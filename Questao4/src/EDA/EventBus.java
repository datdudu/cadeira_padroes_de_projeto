package EDA;

import java.util.*;

public class EventBus {
    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    /**
     * Registra um handler para um tipo de evento específico.
     */
    public void registrar(String tipoEvento, EventHandler handler) {
        handlers.computeIfAbsent(tipoEvento, e -> new ArrayList<>()).add(handler);
        System.out.println("✅ Handler registrado para evento: " + tipoEvento);
    }

    /**
     * Publica um evento (notifica todos os handlers registrados)
     */
    public void publicar(Event evento) {
        System.out.println("\n📢 Publicando evento → " + evento);
        List<EventHandler> lista = handlers.get(evento.getTipo());
        if (lista != null && !lista.isEmpty()) {
            lista.forEach(h -> h.handle(evento));
        } else {
            System.out.println("(Nenhum handler para este tipo de evento)");
        }
    }
}