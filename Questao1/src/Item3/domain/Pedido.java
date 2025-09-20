package Item3.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Aggregate Root
public class Pedido {
    private Long id;
    private String clienteId;
    private StatusPedido status;
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;

    // Construtor privado - só pode ser criado via métodos de negócio
    private Pedido(String clienteId) {
        this.clienteId = clienteId;
        this.status = StatusPedido.RASCUNHO;
        this.itens = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
        this.dataCriacao = LocalDateTime.now();
    }

    // Factory method - única forma de criar um pedido
    public static Pedido criarNovo(String clienteId) {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        return new Pedido(clienteId);
    }
    // ✅ Factory para RESTAURAR pedido (sem itens ainda)
    public static Pedido restaurar(Long id, String clienteId, StatusPedido status,
                                   BigDecimal valorTotal, LocalDateTime dataCriacao) {
        Pedido pedido = new Pedido(clienteId);
        pedido.id = id;
        pedido.status = status;
        pedido.valorTotal = valorTotal;
        pedido.dataCriacao = dataCriacao;
        return pedido;
    }

    // ✅ Factory para RESTAURAR pedido com itens
    public static Pedido restaurarComItens(Long id, String clienteId, StatusPedido status,
                                           BigDecimal valorTotal, LocalDateTime dataCriacao,
                                           List<ItemPedido> itens) {
        Pedido pedido = restaurar(id, clienteId, status, valorTotal, dataCriacao);
        pedido.itens = new ArrayList<>(itens); // encapsula cópia
        return pedido;
    }

    // INVARIANTE 1: Pedido deve ter pelo menos 1 item para ser confirmado
    // INVARIANTE 2: Valor total deve ser sempre a soma dos itens
    // INVARIANTE 3: Só pode adicionar itens se status for RASCUNHO
    public void adicionarItem(String produtoId, int quantidade, BigDecimal precoUnitario) {
        if (status != StatusPedido.RASCUNHO) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido " + status);
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        ItemPedido item = new ItemPedido(produtoId, quantidade, precoUnitario);
        itens.add(item);
        recalcularValorTotal(); // Mantém invariante do valor total
    }

    // INVARIANTE: Só pode confirmar se tiver itens
    public void confirmar() {
        if (status != StatusPedido.RASCUNHO) {
            throw new IllegalStateException("Pedido já foi processado");
        }

        if (itens.isEmpty()) {
            throw new IllegalStateException("Pedido deve ter pelo menos um item para ser confirmado");
        }

        this.status = StatusPedido.CONFIRMADO;
    }

    // INVARIANTE: Valor total sempre correto
    private void recalcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters (sem setters públicos - encapsulamento)
    public Long getId() { return id; }
    public String getClienteId() { return clienteId; }
    public StatusPedido getStatus() { return status; }
    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    // ⚠️ setter package-private (sem modificador, só o pacote "domain" vê)
    void setId(Long id) {
        this.id = id;
    }
}
