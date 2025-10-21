package ReactiveProgramming;

import java.util.concurrent.Flow;

public class MessageSubscriber implements Flow.Subscriber<String> {
    private String nome;
    private Flow.Subscription subscription;

    public MessageSubscriber(String nome) {
        this.nome = nome;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.println("🟢 [" + nome + "] se inscreveu para receber mensagens.");
        subscription.request(1); // pede o primeiro item
    }

    @Override
    public void onNext(String item) {
        System.out.println("🔔 [" + nome + "] recebeu: " + item);
        subscription.request(1); // pede o próximo item (controle de pressão)
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("❌ [" + nome + "] erro: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("✅ [" + nome + "] concluiu o recebimento.");
    }
}