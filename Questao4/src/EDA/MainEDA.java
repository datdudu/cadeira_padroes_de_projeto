package EDA;

public class MainEDA {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        // 1. Módulo de Notificação
        eventBus.registrar("PedidoCriado", e ->
                System.out.println("📩 [Notificação] Enviando e-mail para o cliente do " + e.getDados()));

        // 2. Módulo de Pagamento
        eventBus.registrar("PedidoCriado", e ->
                System.out.println("💰 [Pagamento] Reservando valor do " + e.getDados()));

        // 3. Módulo de Estoque
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🏬 [Estoque] Reduzindo estoque referente ao " + e.getDados()));

        // 4. Módulo de Logística
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🚚 [Logística] Separando produtos do " + e.getDados()));

        // 5. Módulo de Auditoria
        eventBus.registrar("PedidoCancelado", e ->
                System.out.println("⚠️ [Auditoria] Pedido cancelado: " + e.getDados()));

        // --- Publicando eventos ----
        eventBus.publicar(new Event("PedidoCriado", "pedido#123"));
        eventBus.publicar(new Event("PagamentoConfirmado", "pedido#123"));
        eventBus.publicar(new Event("PedidoCancelado", "pedido#987"));
    }
}
