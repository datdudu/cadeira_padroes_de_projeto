package Item3.domain;

import java.math.BigDecimal;

public class ItemPedido {
    private String produtoId;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    // Construtor de domínio (aplicando invariantes)
    public ItemPedido(String produtoId, int quantidade, BigDecimal precoUnitario) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço unitário deve ser positivo");
        }

        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // ✅ Factory para RESTAURAR a partir do banco (já validado)
    public static ItemPedido restaurar(String produtoId, int quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        ItemPedido item = new ItemPedido(produtoId, quantidade, precoUnitario);
        item.subtotal = subtotal; // usa o valor salvo no banco
        return item;
    }

    // Getters
    public String getProdutoId() { return produtoId; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}