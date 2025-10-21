package PubSub;


public class Publisher {
    private Broker broker;
    private String topico;

    public Publisher(Broker broker, String topico) {
        this.broker = broker;
        this.topico = topico;
    }

    /**
     * Publica uma nova mensagem no tópico
     */
    public void publicar(String mensagem) {
        broker.publish(topico, mensagem);
    }
}