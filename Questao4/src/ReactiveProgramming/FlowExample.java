package ReactiveProgramming;

public class FlowExample {
    public static void main(String[] args) throws InterruptedException {
        MessagePublisher publisher = new MessagePublisher();

        // Criação dos subscribers (observadores)
        MessageSubscriber maria = new MessageSubscriber("Maria");
        MessageSubscriber joao = new MessageSubscriber("João");
        MessageSubscriber lucas = new MessageSubscriber("Lucas");

        // Registro dos observadores (assinantes)
        publisher.addSubscriber(maria);
        publisher.addSubscriber(joao);
        publisher.addSubscriber(lucas);

        // Publicação de mensagens (eventos)
        publisher.publish("Evento 1 - Novo artigo disponível");
        publisher.publish("Evento 2 - Oferta de fim de semana");
        publisher.publish("Evento 3 - Atualização de sistema");

        // Fecha o fluxo
        publisher.close();

        // Dá tempo para as threads processarem
        Thread.sleep(500);
        System.out.println("\n🟣 Execução reativa finalizada.");
    }
}