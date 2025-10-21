package PubSub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker {
    // Cada tópico (ou canal) tem uma lista de assinantes (subscribers)
    private Map<String, List<Subscriber>> topicos = new HashMap<>();

    /**
     * Inscreve um subscriber em um tópico
     */
    public void subscribe(String topico, Subscriber sub) {
        topicos.computeIfAbsent(topico, k -> new ArrayList<>()).add(sub);
        System.out.println("📰 " + sub.getNome() + " se inscreveu no tópico: " + topico);
    }

    /**
     * Publica uma mensagem em um tópico
     */
    public void publish(String topico, String mensagem) {
        System.out.println("\n📢 Publicando no tópico '" + topico + "': " + mensagem);
        List<Subscriber> subs = topicos.get(topico);

        if (subs != null) {
            for (Subscriber sub : subs) {
                sub.receberMensagem(topico, mensagem);
            }
        } else {
            System.out.println("(Sem inscritos para este tópico)");
        }
    }
}