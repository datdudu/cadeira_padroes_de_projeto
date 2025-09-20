package Item3.infra.dto;

import java.math.BigDecimal;

public class ItemRequest {
    private String produtoId;
    private int quantidade;
    private BigDecimal preco;

    public ItemRequest(String produtoId, int quantidade, BigDecimal preco) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public String getProdutoId() { return produtoId; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPreco() { return preco; }
}