package Item3.infra.service;

import Item3.domain.Pedido;
import Item3.domain.repository.PedidoRepository;
import Item3.infra.dto.ItemRequest;

import java.math.BigDecimal;
import java.util.List;

public class PedidoService {
    private PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public void criarPedido(String clienteId, List<ItemRequest> itensRequest) {
        // Cria o agregado
        Pedido pedido = Pedido.criarNovo(clienteId);

        // Adiciona itens (invariantes são validadas automaticamente)
        for (ItemRequest itemReq : itensRequest) {
            pedido.adicionarItem(itemReq.getProdutoId(),
                    itemReq.getQuantidade(),
                    itemReq.getPreco());
        }

        // Confirma o pedido (valida invariante: deve ter itens)
        pedido.confirmar();

        // Salva o agregado completo
        repository.salvar(pedido);
    }

    public void adicionarItemAoPedido(Long pedidoId, String produtoId, int quantidade, BigDecimal preco) {
        // Recupera o agregado completo
        Pedido pedido = repository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Modifica através da raiz do agregado (invariantes protegidas)
        pedido.adicionarItem(produtoId, quantidade, preco);

        // Salva o agregado completo
        repository.salvar(pedido);
    }
}