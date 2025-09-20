package Item3.domain.repository;

import Item3.domain.Pedido;

import java.util.List;
import java.util.Optional;

// Repository gerencia APENAS o agregado Pedido (não ItemPedido individualmente)
public interface PedidoRepository {
    void salvar(Pedido pedido);           // Salva o agregado completo
    Optional<Pedido> buscarPorId(Long id); // Recupera o agregado completo
    List<Pedido> buscarPorCliente(String clienteId);
    void remover(Pedido pedido);          // Remove o agregado completo
}
