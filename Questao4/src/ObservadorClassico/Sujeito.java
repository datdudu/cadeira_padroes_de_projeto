package ObservadorClassico;

public interface Sujeito {
    void registrar(Observador o);
    void remover(Observador o);
    void notificar(String mensagem);
}
