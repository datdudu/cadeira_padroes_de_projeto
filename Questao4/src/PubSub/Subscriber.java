package PubSub;


public class Subscriber {
    private String nome;

    public Subscriber(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    /**
     * Método chamado pelo broker quando uma nova mensagem é publicada
     */
    public void receberMensagem(String topico, String mensagem) {
        System.out.println("🔔 [" + nome + "] recebeu no tópico '" + topico + "': " + mensagem);
    }
}