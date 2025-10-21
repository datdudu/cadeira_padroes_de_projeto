package EDA;

public class Event {
    private String tipo;
    private String dados;

    public Event(String tipo, String dados) {
        this.tipo = tipo;
        this.dados = dados;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDados() {
        return dados;
    }

    @Override
    public String toString() {
        return "Event{" +
                "tipo='" + tipo + '\'' +
                ", dados='" + dados + '\'' +
                '}';
    }
}
