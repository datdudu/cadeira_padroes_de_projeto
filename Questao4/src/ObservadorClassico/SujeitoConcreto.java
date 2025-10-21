package ObservadorClassico;

import java.util.ArrayList;
import java.util.List;

public class SujeitoConcreto implements Sujeito {
    private List<Observador> observadores = new ArrayList<>();

    @Override
    public void registrar(Observador o) {
        observadores.add(o);
    }

    @Override
    public void remover(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notificar(String mensagem) {
        for (Observador o : observadores) {
            o.atualizar(mensagem);
        }
    }

    // Método específico de negócio — gera um novo evento
    public void novaMensagem(String msg) {
        System.out.println("📢 Sujeito gerou novo evento: " + msg);
        notificar(msg);
    }
}