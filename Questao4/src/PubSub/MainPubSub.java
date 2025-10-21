package PubSub;

public class MainPubSub {
    public static void main(String[] args) {
        Broker broker = new Broker();

        // Criação de subscribers
        Subscriber alice = new Subscriber("Alice");
        Subscriber bruno = new Subscriber("Bruno");
        Subscriber carla = new Subscriber("Carla");

        // Inscrições nos tópicos
        broker.subscribe("esportes", alice);
        broker.subscribe("noticias", bruno);
        broker.subscribe("esportes", carla);

        // Criação de publishers
        Publisher pubEsportes = new Publisher(broker, "esportes");
        Publisher pubNoticias = new Publisher(broker, "noticias");

        // Publicações
        pubEsportes.publicar("⚽️ Resultado do jogo: 3x1");
        pubNoticias.publicar("🗞️ Nova descoberta científica!");
        pubEsportes.publicar("🏃 Evento de corrida confirmado para domingo!");
    }
}