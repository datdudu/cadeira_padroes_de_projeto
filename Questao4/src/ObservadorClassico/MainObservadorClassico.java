package ObservadorClassico;
public class MainObservadorClassico {
    public static void main(String[] args) {

        // Cria o sujeito (objeto observado)
        SujeitoConcreto canalNotificacoes = new SujeitoConcreto();

        // Cria e registra observadores
        Observador ana = new ObservadorConcreto("Ana");
        Observador bruno = new ObservadorConcreto("Bruno");
        Observador carla = new ObservadorConcreto("Carla");

        canalNotificacoes.registrar(ana);
        canalNotificacoes.registrar(bruno);
        canalNotificacoes.registrar(carla);

        // Simula eventos
        canalNotificacoes.novaMensagem("Novo vídeo disponível!");
        canalNotificacoes.novaMensagem("Promoção imperdível!");
        canalNotificacoes.novaMensagem("Live começando agora!");

        // Remove um observador e notifica novamente
        canalNotificacoes.remover(bruno);
        System.out.println("\n--- Bruno saiu das notificações ---\n");

        canalNotificacoes.novaMensagem("Nova enquete postada!");
    }
}