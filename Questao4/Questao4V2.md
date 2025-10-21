### Questão 4 — O padrão Observer é onipresente

#### Introdução

O padrão Observer define uma forma desacoplada de notificar múltiplos interessados (observadores) quando um estado muda. Ele é a raiz conceitual por trás de diversas tecnologias modernas: desde listeners em GUIs, passando por brokers de mensagens em Pub/Sub, até fluxos reativos (Reactive Streams) e arquiteturas orientadas a eventos (EDA).

Nesta questão, mostramos o Observer clássico e comparamos seu código com três tecnologias derivadas:

- Publish–Subscribe (Pub/Sub)
- Reactive Programming (Java Flow API)
- Arquitetura Orientada a Eventos (EDA)

Para cada uma, apresentamos:
- Código essencial
- Semelhanças com o Observer clássico
- Diferenças importantes

Ao final, incluímos um diagrama UML textual comparativo da evolução conceitual.

---

### 1) Observer clássico (direto 1 → N, local e síncrono)

No padrão original, um Sujeito mantém uma lista de Observadores e os notifica diretamente quando ocorre uma mudança.

#### Código (pacote: observer_classico)

Observador.java
```java
package observer_classico;

public interface Observador {
    void atualizar(String mensagem);
}
```

Sujeito.java
```java
package observer_classico;

public interface Sujeito {
    void registrar(Observador o);
    void remover(Observador o);
    void notificar(String mensagem);
}
```

SujeitoConcreto.java
```java
package observer_classico;

import java.util.ArrayList;
import java.util.List;

public class SujeitoConcreto implements Sujeito {
    private List<Observador> observadores = new ArrayList<>();

    @Override
    public void registrar(Observador o) { observadores.add(o); }

    @Override
    public void remover(Observador o) { observadores.remove(o); }

    @Override
    public void notificar(String mensagem) {
        for (Observador o : observadores) {
            o.atualizar(mensagem);
        }
    }

    public void novaMensagem(String msg) {
        System.out.println("📢 Sujeito gerou novo evento: " + msg);
        notificar(msg);
    }
}
```

ObservadorConcreto.java
```java
package observer_classico;

public class ObservadorConcreto implements Observador {
    private String nome;

    public ObservadorConcreto(String nome) { this.nome = nome; }

    @Override
    public void atualizar(String mensagem) {
        System.out.println("🔔 [" + nome + "] recebeu a atualização: " + mensagem);
    }
}
```

Main.java
```java
package observer_classico;

public class Main {
    public static void main(String[] args) {
        SujeitoConcreto canal = new SujeitoConcreto();

        canal.registrar(new ObservadorConcreto("Ana"));
        canal.registrar(new ObservadorConcreto("Bruno"));

        canal.novaMensagem("Novo vídeo lançado!");
        canal.novaMensagem("Enquete disponível!");
    }
}
```

Semelhanças e diferenças (referência base para comparação):
- Semelhança (conceito base): 1→N, um “emissor” notifica vários “receptores”.
- Diferença: comunicação direta e local; observadores precisam ser registrados no próprio sujeito; normalmente síncrono.

---

### 2) Publish–Subscribe (generaliza o Observer com um broker intermediário)

O Pub/Sub é a generalização do Observer. Em vez do sujeito notificar diretamente os observadores, um Publisher publica mensagens em tópicos para um Broker, e Subscribers inscritos nesses tópicos as recebem. Isso desacopla completamente emissores e receptores e permite N→N.

Exemplos reais: JMS, RabbitMQ, Apache Kafka, Google Pub/Sub, AWS SNS/SQS, Azure Event Hub, MQTT (IoT).

#### Código (pacote: pubsub)

Broker.java
```java
package pubsub;

import java.util.*;

public class Broker {
    private Map<String, List<Subscriber>> topicos = new HashMap<>();

    public void subscribe(String topico, Subscriber sub) {
        topicos.computeIfAbsent(topico, k -> new ArrayList<>()).add(sub);
        System.out.println("📰 " + sub.getNome() + " se inscreveu no tópico: " + topico);
    }

    public void publish(String topico, String mensagem) {
        System.out.println("\n📢 Publicando no tópico '" + topico + "': " + mensagem);
        List<Subscriber> subs = topicos.get(topico);
        if (subs != null) subs.forEach(s -> s.receberMensagem(topico, mensagem));
        else System.out.println("(Sem inscritos para este tópico)");
    }
}
```

Subscriber.java
```java
package pubsub;

public class Subscriber {
    private String nome;

    public Subscriber(String nome) { this.nome = nome; }
    public String getNome() { return nome; }

    public void receberMensagem(String topico, String mensagem) {
        System.out.println("🔔 [" + nome + "] recebeu no tópico '" + topico + "': " + mensagem);
    }
}
```

Publisher.java
```java
package pubsub;

public class Publisher {
    private Broker broker;
    private String topico;

    public Publisher(Broker broker, String topico) {
        this.broker = broker;
        this.topico = topico;
    }

    public void publicar(String mensagem) {
        broker.publish(topico, mensagem);
    }
}
```

MainPubSub.java
```java
package pubsub;

public class MainPubSub {
    public static void main(String[] args) {
        Broker broker = new Broker();

        Subscriber alice = new Subscriber("Alice");
        Subscriber bruno = new Subscriber("Bruno");
        Subscriber carla = new Subscriber("Carla");

        broker.subscribe("esportes", alice);
        broker.subscribe("noticias", bruno);
        broker.subscribe("esportes", carla);

        Publisher pubEsportes = new Publisher(broker, "esportes");
        Publisher pubNoticias = new Publisher(broker, "noticias");

        pubEsportes.publicar("⚽️ Resultado do jogo: 3x1");
        pubNoticias.publicar("🗞️ Nova descoberta científica!");
        pubEsportes.publicar("🏃 Evento de corrida confirmado!");
    }
}
```

Semelhanças com o Observer clássico:
- Mesmo princípio: um emissor publica eventos e múltiplos receptores são notificados.
- Multiplicidade: comunicação 1→N (e também N→N).

Diferenças:
- Intermediário (Broker): Publisher e Subscriber não se conhecem.
- Desacoplamento: muito maior; ideal para sistemas distribuídos.
- Tópicos/Filas: roteamento por assunto; entrega assíncrona; escalabilidade horizontal.

---

### 3) Eventos e Reactive Programming (Java Flow API – Observer reativo com backpressure)

Nos streams reativos, o Observer vira a base da programação: fluxos contínuos de notificações assíncronas com controle de pressão (backpressure). A Flow API (Java 9+) é a padronização oficial de Reactive Streams no Java.

Exemplos reais: ReactiveX (RxJava, RxJS, RxSwift), Project Reactor (Spring WebFlux), Akka Streams, Java Flow API, Kotlin Flow.

#### Código (pacote: reactive_flow)

MessageSubscriber.java
```java
package reactive_flow;

import java.util.concurrent.Flow;

public class MessageSubscriber implements Flow.Subscriber<String> {
    private String nome;
    private Flow.Subscription subscription;

    public MessageSubscriber(String nome) { this.nome = nome; }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        System.out.println("🟢 [" + nome + "] se inscreveu.");
        subscription.request(1); // backpressure: consome no seu ritmo
    }

    @Override
    public void onNext(String item) {
        System.out.println("🔔 [" + nome + "] recebeu: " + item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("❌ [" + nome + "] erro: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("✅ [" + nome + "] concluiu.");
    }
}
```

MessagePublisher.java
```java
package reactive_flow;

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
```

FlowExample.java
```java
package reactive_flow;

public class FlowExample {
    public static void main(String[] args) throws InterruptedException {
        MessagePublisher publisher = new MessagePublisher();

        publisher.addSubscriber(new MessageSubscriber("Maria"));
        publisher.addSubscriber(new MessageSubscriber("João"));
        publisher.addSubscriber(new MessageSubscriber("Lucas"));

        publisher.publish("Evento 1 - Novo artigo");
        publisher.publish("Evento 2 - Oferta");
        publisher.publish("Evento 3 - Atualização");

        publisher.close();

        Thread.sleep(500);
        System.out.println("\n🟣 Execução reativa finalizada.");
    }
}
```

Semelhanças com o Observer clássico:
- Publisher notifica Subscribers quando há novos eventos.
- Multiplicidade 1→N persiste.

Diferenças:
- Assíncrono, non-blocking, com backpressure (Subscriber controla ritmo via `request(n)`).
- Contratos padronizados (Flow.Publisher/Subscriber/Subscription/Processor).
- Base para programação com streams contínuas (pipelines reativos, transformação, composição).

---

### 4) Arquiteturas orientadas a eventos (EDA – Observer em nível arquitetural)

Na EDA, o Observer é expandido para o nível de sistema: diferentes módulos (ou microserviços) publicam e reagem a eventos de domínio via um barramento de eventos (Event Bus). É o alicerce de CQRS, Event Sourcing e Serverless Functions.

Exemplos reais: Event Sourcing (Event Store, Axon Framework), CQRS, Serverless (AWS Lambda, GCP Functions), Kafka como backbone de eventos.

#### Código (pacote: eda)

Event.java
```java
package eda;

public class Event {
    private String tipo;
    private String dados;

    public Event(String tipo, String dados) {
        this.tipo = tipo;
        this.dados = dados;
    }
    public String getTipo() { return tipo; }
    public String getDados() { return dados; }

    @Override
    public String toString() {
        return "Event{tipo='" + tipo + "', dados='" + dados + "'}";
    }
}
```

EventHandler.java
```java
package eda;

public interface EventHandler {
    void handle(Event evento);
}
```

EventBus.java
```java
package eda;

import java.util.*;

public class EventBus {
    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    public void registrar(String tipoEvento, EventHandler handler) {
        handlers.computeIfAbsent(tipoEvento, e -> new ArrayList<>()).add(handler);
        System.out.println("✅ Handler registrado para: " + tipoEvento);
    }

    public void publicar(Event evento) {
        System.out.println("\n📢 Publicando evento → " + evento);
        List<EventHandler> lista = handlers.get(evento.getTipo());
        if (lista == null || lista.isEmpty()) {
            System.out.println("(Nenhum handler para este tipo)");
            return;
        }
        lista.forEach(h -> h.handle(evento));
    }
}
```

MainEDA.java
```java
package eda;

public class MainEDA {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        eventBus.registrar("PedidoCriado", e ->
                System.out.println("📩 [Notificação] E-mail para cliente do " + e.getDados()));
        eventBus.registrar("PedidoCriado", e ->
                System.out.println("💰 [Pagamento] Reservando valor do " + e.getDados()));
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🏬 [Estoque] Reduzindo estoque do " + e.getDados()));
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🚚 [Logística] Separando itens do " + e.getDados()));
        eventBus.registrar("PedidoCancelado", e ->
                System.out.println("⚠️ [Auditoria] Pedido cancelado: " + e.getDados()));

        eventBus.publicar(new Event("PedidoCriado", "pedido#123"));
        eventBus.publicar(new Event("PagamentoConfirmado", "pedido#123"));
        eventBus.publicar(new Event("PedidoCancelado", "pedido#987"));
    }
}
```

Semelhanças com o Observer clássico:
- Mesmo princípio de notificação multiassinante: eventos disparam reações em vários handlers.
- A semântica de “ouvir e reagir a eventos” permanece.

Diferenças:
- Nível arquitetural: eventos de domínio, múltiplos módulos/serviços.
- Desacoplamento extremo: produtores e consumidores não se conhecem.
- Persistência e orquestração possíveis: Event Sourcing, CQRS, Saga, Serverless triggers.

---

### Diagrama comparativo UML textual — evolução conceitual (Observer → Pub/Sub → Reactive → EDA)

Observer clássico
```
[Sujeito] <>-- (1..*) [Observador]
SujeitoConcreto --|> Sujeito
ObservadorConcreto --|> Observador
SujeitoConcreto --> ObservadorConcreto : notificar(mensagem)
```

Publish–Subscribe (generalizado via broker)
```
[Publisher] --> [Broker] --> [Subscriber]
Broker "1" o-- "*" Subscriber
Broker "1" o-- "*" Publisher
note right of Broker
  - Mantém tópicos
  - Distribui mensagens
  - Desacopla emissores/receptores
end note
```

Reactive Programming (Java Flow API)
```
Flow.Publisher --> Flow.Subscriber
Flow.Subscriber <--> Flow.Subscription : request(n) / cancel()
note right
  - Assíncrono, non-blocking
  - Backpressure (controle de fluxo)
  - Streams contínuas
end note
```

Event-Driven Architecture (EDA)
```
[EventProducer] --> [EventBus] --> [EventHandler]
[EventBus] o-- "*" EventHandler
[EventBus] o-- "*" Event
note right of EventBus
  - Eventos de domínio
  - Módulos independentes
  - Base para CQRS / Event Sourcing / Serverless
end note
```

---

### Tabela-resumo (semântica e diferenças principais)

- Observer clássico:
    - Direto, 1→N, local, síncrono.
    - Listeners/handlers dentro do processo.

- Publish–Subscribe:
    - Generaliza o Observer com broker.
    - N→N, via tópicos/filas, assincronia e distribuição.
    - Exemplos: JMS, RabbitMQ, Kafka, Google Pub/Sub, AWS SNS/SQS, Azure Event Hub, MQTT.

- Reactive (Flow API):
    - Observer como base de fluxos assíncronos contínuos com backpressure.
    - Exemplos: RxJava, Reactor (WebFlux), Akka Streams, Java Flow API, Kotlin Flow.

- EDA:
    - Observer em nível arquitetural.
    - Eventos de domínio, múltiplos serviços, alta escalabilidade.
    - Exemplos: Event Sourcing (Axon), CQRS, Serverless (AWS Lambda, GCP Functions).

---

### Conclusão

Tudo é Observer em diferentes níveis de abstração:

- Direto (1→N) → Observer clássico
- Generalizado (N→N) → Pub/Sub (brokers, MQs)
- Reativo → Rx, Reactor, Flow API
- Arquitetural → Event Sourcing, CQRS, EDA

Ou seja:
Observer → Pub/Sub → Reactive → Event-driven architecture

A raiz conceitual é a mesma: mudanças geram eventos que notificam interessados de forma desacoplada. A diferença está no alcance (local vs. distribuído), no intermediário (broker/event bus), e na natureza do fluxo (pontual vs. contínuo/reativo).