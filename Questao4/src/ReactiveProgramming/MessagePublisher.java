package ReactiveProgramming;

import java.util.concurrent.SubmissionPublisher;

public class MessagePublisher {
    private SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    public void addSubscriber(MessageSubscriber subscriber) {
        publisher.subscribe(subscriber);
    }

    public void publish(String mensagem) {
        System.out.println("\n📣 Publicando: " + mensagem);
        publisher.submit(mensagem);
    }

    public void close() {
        publisher.close();
    }
}