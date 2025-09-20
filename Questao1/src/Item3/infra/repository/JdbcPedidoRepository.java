package Item3.infra.repository;

import Item3.domain.ItemPedido;
import Item3.domain.Pedido;
import Item3.domain.StatusPedido;
import Item3.domain.repository.PedidoRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPedidoRepository implements PedidoRepository {
    private Connection connection;

    public JdbcPedidoRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Pedido pedido) {
        try {
            connection.setAutoCommit(false); // Transação para manter consistência

            if (pedido.getId() == null) {
                inserirPedido(pedido);
            } else {
                atualizarPedido(pedido);
            }

            // Salva todos os itens do agregado
            salvarItens(pedido);

            connection.commit(); // Agregado salvo como unidade
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Erro no rollback", rollbackEx);
            }
            throw new RuntimeException("Erro ao salvar pedido", e);
        }
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        // Recupera o agregado COMPLETO (pedido + todos os itens)
        String sqlPedido = "SELECT * FROM pedidos WHERE id = ?";
        String sqlItens = "SELECT * FROM itens_pedido WHERE pedido_id = ?";

        try (PreparedStatement stmtPedido = connection.prepareStatement(sqlPedido);
             PreparedStatement stmtItens = connection.prepareStatement(sqlItens)) {

            stmtPedido.setLong(1, id);
            ResultSet rsPedido = stmtPedido.executeQuery();

            if (!rsPedido.next()) {
                return Optional.empty();
            }

            // Reconstrói o agregado usando factory method
            Pedido pedido = mapearPedido(rsPedido);

            // Busca e adiciona os itens
            stmtItens.setLong(1, id);
            ResultSet rsItens = stmtItens.executeQuery();

            List<ItemPedido> itens = new ArrayList<>();
            while (rsItens.next()) {
                ItemPedido item = mapearItem(rsItens);
                itens.add(item);
            }

            // Usa factory method para restaurar com itens
            pedido = Pedido.restaurarComItens(
                    pedido.getId(),
                    pedido.getClienteId(),
                    pedido.getStatus(),
                    pedido.getValorTotal(),
                    pedido.getDataCriacao(),
                    itens
            );

            return Optional.of(pedido);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido", e);
        }
    }

    @Override
    public List<Pedido> buscarPorCliente(String clienteId) {
        String sql = "SELECT * FROM pedidos WHERE cliente_id = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);

                // Para cada pedido, busca seus itens
                List<ItemPedido> itens = buscarItensDoPedido(pedido.getId());

                // Reconstrói com itens
                pedido = Pedido.restaurarComItens(
                        pedido.getId(),
                        pedido.getClienteId(),
                        pedido.getStatus(),
                        pedido.getValorTotal(),
                        pedido.getDataCriacao(),
                        itens
                );

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos por cliente", e);
        }

        return pedidos;
    }

    @Override
    public void remover(Pedido pedido) {
        try {
            connection.setAutoCommit(false);

            // Remove itens primeiro (FK constraint)
            String deleteItens = "DELETE FROM itens_pedido WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteItens)) {
                stmt.setLong(1, pedido.getId());
                stmt.executeUpdate();
            }

            // Remove o pedido
            String deletePedido = "DELETE FROM pedidos WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deletePedido)) {
                stmt.setLong(1, pedido.getId());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Erro no rollback", rollbackEx);
            }
            throw new RuntimeException("Erro ao remover pedido", e);
        }
    }

    private void inserirPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (cliente_id, status, valor_total, data_criacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pedido.getClienteId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.setBigDecimal(3, pedido.getValorTotal());
            stmt.setTimestamp(4, Timestamp.valueOf(pedido.getDataCriacao()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Long novoId = rs.getLong(1);

                // ✅ Usando factory method para criar nova instância com ID
                // Substitui o pedido atual por uma nova instância com ID
                Pedido pedidoComId = Pedido.restaurarComItens(
                        novoId,
                        pedido.getClienteId(),
                        pedido.getStatus(),
                        pedido.getValorTotal(),
                        pedido.getDataCriacao(),
                        pedido.getItens()
                );

                // Copia os dados de volta (alternativa: retornar o novo pedido)
                // Aqui assumimos que existe um método para isso ou usamos reflection mínima
                copiarDados(pedidoComId, pedido);
            }
        }
    }

    private void atualizarPedido(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET cliente_id = ?, status = ?, valor_total = ?, data_criacao = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pedido.getClienteId());
            stmt.setString(2, pedido.getStatus().name());
            stmt.setBigDecimal(3, pedido.getValorTotal());
            stmt.setTimestamp(4, Timestamp.valueOf(pedido.getDataCriacao()));
            stmt.setLong(5, pedido.getId());

            stmt.executeUpdate();
        }
    }

    private void salvarItens(Pedido pedido) throws SQLException {
        // Remove itens antigos
        String deleteSql = "DELETE FROM itens_pedido WHERE pedido_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, pedido.getId());
            deleteStmt.executeUpdate();
        }

        // Insere itens atuais
        String insertSql = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            for (ItemPedido item : pedido.getItens()) {
                insertStmt.setLong(1, pedido.getId());
                insertStmt.setString(2, item.getProdutoId());
                insertStmt.setInt(3, item.getQuantidade());
                insertStmt.setBigDecimal(4, item.getPrecoUnitario());
                insertStmt.setBigDecimal(5, item.getSubtotal());
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    private List<ItemPedido> buscarItensDoPedido(Long pedidoId) throws SQLException {
        String sql = "SELECT * FROM itens_pedido WHERE pedido_id = ?";
        List<ItemPedido> itens = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                itens.add(mapearItem(rs));
            }
        }

        return itens;
    }

    // ✅ Implementação completa do mapeamento usando factory method
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        return Pedido.restaurar(
                rs.getLong("id"),
                rs.getString("cliente_id"),
                StatusPedido.valueOf(rs.getString("status")),
                rs.getBigDecimal("valor_total"),
                rs.getTimestamp("data_criacao").toLocalDateTime()
        );
    }

    // ✅ Implementação completa do mapeamento de item
    private ItemPedido mapearItem(ResultSet rs) throws SQLException {
        return ItemPedido.restaurar(
                rs.getString("produto_id"),
                rs.getInt("quantidade"),
                rs.getBigDecimal("preco_unitario"),
                rs.getBigDecimal("subtotal")
        );
    }

    // Método auxiliar para copiar dados (alternativa ao reflection)
    private void copiarDados(Pedido origem, Pedido destino) {
        // Aqui você implementaria a cópia dos dados necessários
        // ou mudaria a arquitetura para retornar o novo pedido
        // Por simplicidade, assumimos que existe um método no domínio para isso
        // ou usamos reflection mínima apenas para o ID
        try {
            java.lang.reflect.Field idField = destino.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(destino, origem.getId());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao copiar ID", e);
        }
    }
}