### Questão 4 — O padrão Observer é onipresente

#### Introdução

O padrão Observer define como um objeto (Sujeito) notifica automaticamente múltiplos objetos interessados (Observadores) quando há mudanças.
Ele é a raiz conceitual de muitas tecnologias modernas: Pub/Sub (com broker), programação reativa (streams assíncronas) e arquiteturas orientadas a eventos (EDA).

Nesta resposta, mostramos:

- O Observer clássico como referência
- Três tecnologias derivadas (Pub/Sub, Reactive com Java Flow API e EDA)
- Código comentado em cada uma, explicando como funciona e comparando com o Observer clássico

Ao final, há um diagrama UML textual que relaciona os quatro modelos.

---

### 1) Observer clássico (direto 1 → N, local e síncrono)

Contexto: Um “Sujeito” gerencia a lista de “Observadores” e os notifica diretamente quando algo muda. É a base para listeners de GUI e handlers de eventos em bibliotecas tradicionais.

#### Código comentado (pacote: observer_classico)

Observador.java
```java
package observer_classico;

// Contrato que todo observador precisa implementar.
// O Sujeito chamará este método para avisar sobre novidades.
public interface Observador {
    void atualizar(String mensagem);
}
```

Sujeito.java
```java
package observer_classico;

// Contrato do sujeito observado.
// Permite registrar/remover observadores e notificar todos.
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

// Implementação concreta do sujeito.
// Mantém uma lista de observadores e notifica todos quando há um evento.
public class SujeitoConcreto implements Sujeito {
    private List<Observador> observadores = new ArrayList<>();

    @Override
    public void registrar(Observador o) { 
        // Observadores são adicionados explicitamente
        observadores.add(o); 
    }

    @Override
    public void remover(Observador o) { 
        // Podem sair a qualquer momento
        observadores.remove(o); 
    }

    @Override
    public void notificar(String mensagem) {
        // Notificação síncrona e direta a todos os inscritos
        for (Observador o : observadores) {
            o.atualizar(mensagem);
        }
    }

    // Representa um "evento" de negócio: quando algo novo acontece,
    // o Sujeito chama notificar() para avisar todo mundo.
    public void novaMensagem(String msg) {
        System.out.println("📢 Sujeito gerou novo evento: " + msg);
        notificar(msg);
    }
}
```

ObservadorConcreto.java
```java
package observer_classico;

// Exemplo de observador real que reage às notificações
public class ObservadorConcreto implements Observador {
    private String nome;

    public ObservadorConcreto(String nome) { this.nome = nome; }

    @Override
    public void atualizar(String mensagem) {
        // Reação simples: imprimir o conteúdo recebido
        System.out.println("🔔 [" + nome + "] recebeu a atualização: " + mensagem);
    }
}
```

Main.java
```java
package observer_classico;

// Demonstração: Registramos observadores e geramos eventos.
// A comunicação é direta (Sujeito -> Observadores) e síncrona (no mesmo thread).
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

Semelhanças e diferenças (referência base):
- Semelhança conceitual (com todos os modelos): um emissor aciona múltiplos receptores.
- Diferença: acoplamento e entrega direta; observadores são registrados no sujeito; síncrono e local (mesmo processo).

---

### 2) Publish–Subscribe (generaliza o Observer com broker intermediário)

Contexto: O Pub/Sub insere um “Broker” no meio. Publishers publicam mensagens em tópicos, e Subscribers recebem as mensagens via broker se estiverem inscritos. O Publisher não conhece os Subscribers.

Exemplos: JMS, RabbitMQ, Apache Kafka, Google Pub/Sub, AWS SNS/SQS, Azure Event Hub, MQTT.

Como se relaciona ao Observer:
- Semelhança: um emissor gera eventos consumidos por vários receptores.
- Diferença: há um intermediário (broker), que desacopla fortemente as partes; suporta N→N, roteamento por tópicos e distribuição assíncrona.

#### Código comentado (pacote: pubsub)

Broker.java
```java
package pubsub;

import java.util.*;

// "Broker": intermediário que gerencia tópicos e assinantes.
// É ele quem recebe publicações e as repassa aos inscritos no tópico.
public class Broker {
    private Map<String, List<Subscriber>> topicos = new HashMap<>();

    // Inscreve um subscriber em um tópico específico
    public void subscribe(String topico, Subscriber sub) {
        topicos.computeIfAbsent(topico, k -> new ArrayList<>()).add(sub);
        System.out.println("📰 " + sub.getNome() + " se inscreveu no tópico: " + topico);
    }

    // Publica uma mensagem no tópico: o broker entrega a todos os inscritos
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

// "Subscriber": receptor de mensagens do broker.
// Não conhece o Publisher, apenas reage às mensagens do tópico.
public class Subscriber {
    private String nome;

    public Subscriber(String nome) { this.nome = nome; }
    public String getNome() { return nome; }

    // Método chamado pelo broker ao publicar no tópico
    public void receberMensagem(String topico, String mensagem) {
        System.out.println("🔔 [" + nome + "] recebeu no tópico '" + topico + "': " + mensagem);
    }
}
```

Publisher.java
```java
package pubsub;

// "Publisher": produtor que publica mensagens em um tópico via broker.
// Não conhece nenhum subscriber diretamente.
public class Publisher {
    private Broker broker;
    private String topico;

    public Publisher(Broker broker, String topico) {
        this.broker = broker;
        this.topico = topico;
    }

    // Publica a mensagem no tópico escolhido; delivery é responsabilidade do broker
    public void publicar(String mensagem) {
        broker.publish(topico, mensagem);
    }
}
```

MainPubSub.java
```java
package pubsub;

// Demonstração: Subscribers se inscrevem em tópicos no broker.
// Publishers publicam mensagens; o broker entrega a quem está inscrito.
public class MainPubSub {
    public static void main(String[] args) {
        Broker broker = new Broker();

        Subscriber alice = new Subscriber("Alice");
        Subscriber bruno = new Subscriber("Bruno");
        Subscriber carla = new Subscriber("Carla");

        // Inscrições em tópicos
        broker.subscribe("esportes", alice);
        broker.subscribe("noticias", bruno);
        broker.subscribe("esportes", carla);

        // Dois publishers publicando em tópicos diferentes
        Publisher pubEsportes = new Publisher(broker, "esportes");
        Publisher pubNoticias = new Publisher(broker, "noticias");

        pubEsportes.publicar("⚽️ Resultado do jogo: 3x1");
        pubNoticias.publicar("🗞️ Nova descoberta científica!");
        pubEsportes.publicar("🏃 Evento de corrida confirmado!");
    }
}
```

Resumo da comparação:
- Semelhança com Observer: eventos disparam notificações a múltiplos receptores.
- Diferença: intermediação via broker; alto desacoplamento; roteamento por tópicos; pronto para ambientes distribuídos.

---

### 3) Eventos e Reactive Programming (Java Flow API – Observer reativo com backpressure)

Contexto: Programação reativa usa o Observer como base para streams assíncronas contínuas. A Flow API (Java 9+) padroniza Publisher, Subscriber, Subscription e Processor, suportando backpressure (controle do ritmo).

Exemplos: ReactiveX (RxJava, RxJS, RxSwift), Project Reactor (WebFlux), Akka Streams, Java Flow API, Kotlin Flow.

Como se relaciona ao Observer:
- Semelhança: Subscribers reagem a eventos emitidos por um Publisher (1→N).
- Diferença: assíncrono/non-blocking; controle de fluxo com `request(n)`; streams contínuas e composição.

#### Código comentado (pacote: reactive_flow)

MessageSubscriber.java
```java
package reactive_flow;

import java.util.concurrent.Flow;

// Subscriber reativo: implementa o contrato Flow.Subscriber.
// Recebe eventos e controla o ritmo de consumo via Subscription (backpressure).
public class MessageSubscriber implements Flow.Subscriber<String> {
    private String nome;
    private Flow.Subscription subscription;

    public MessageSubscriber(String nome) { this.nome = nome; }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        // Após se inscrever, recebe uma Subscription para controlar o fluxo
        this.subscription = subscription;
        System.out.println("🟢 [" + nome + "] se inscreveu.");
        subscription.request(1); // pede o primeiro item (consumo sob demanda)
    }

    @Override
    public void onNext(String item) {
        // Reage a cada item emitido pelo Publisher
        System.out.println("🔔 [" + nome + "] recebeu: " + item);
        subscription.request(1); // pede mais um (fluxo sob controle do consumidor)
    }

    @Override
    public void onError(Throwable throwable) {
        // Tratamento de erro no pipeline reativo
        System.out.println("❌ [" + nome + "] erro: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        // Chamado quando o Publisher fecha o stream
        System.out.println("✅ [" + nome + "] concluiu.");
    }
}
```

MessagePublisher.java
```java
package reactive_flow;

import java.util.concurrent.SubmissionPublisher;

// Publisher reativo pronto da JDK: SubmissionPublisher.
// Permite publicar itens assíncronos e gerencia threads internas.
public class MessagePublisher {
    private SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    // Registro de assinantes (observadores)
    public void addSubscriber(MessageSubscriber subscriber) {
        publisher.subscribe(subscriber);
    }

    // Emissão de um novo item no stream
    public void publish(String mensagem) {
        System.out.println("\n📣 Publicando: " + mensagem);
        publisher.submit(mensagem);
    }

    // Indica que não haverá mais itens
    public void close() {
        publisher.close();
    }
}
```

FlowExample.java
```java
package reactive_flow;

// Demonstração: vários subscribers reagem a uma sequência de eventos.
// Execução é assíncrona; cada subscriber controla seu ritmo via backpressure.
public class FlowExample {
    public static void main(String[] args) throws InterruptedException {
        MessagePublisher publisher = new MessagePublisher();

        publisher.addSubscriber(new MessageSubscriber("Maria"));
        publisher.addSubscriber(new MessageSubscriber("João"));
        publisher.addSubscriber(new MessageSubscriber("Lucas"));

        // Emite alguns eventos no stream
        publisher.publish("Evento 1 - Novo artigo");
        publisher.publish("Evento 2 - Oferta");
        publisher.publish("Evento 3 - Atualização");

        // Finaliza o stream
        publisher.close();

        // Aguardar processamento assíncrono
        Thread.sleep(500);
        System.out.println("\n🟣 Execução reativa finalizada.");
    }
}
```

Resumo da comparação:
- Semelhança com Observer: Publisher notifica vários Subscribers.
- Diferença: modelo reativo padronizado; assíncrono; controle de backpressure; ideal para pipelines reativos (WebFlux, RxJava).

---

### 4) Arquiteturas orientadas a eventos (EDA – Observer em nível arquitetural)

Contexto: Na EDA, eventos são “fatos do domínio” que orquestram módulos independentes (até microserviços). Um EventBus registra handlers e distribui eventos. É a base de CQRS, Event Sourcing e serverless.

Exemplos: Event Sourcing (Event Store, Axon Framework), CQRS, Serverless (AWS Lambda, GCP Functions), Kafka como backbone.

Como se relaciona ao Observer:
- Semelhança: eventos disparam múltiplas reações independentes.
- Diferença: nível arquitetural; módulos separados; alto desacoplamento; pode haver persistência de eventos, sagas e orquestrações.

#### Código comentado (pacote: eda)

Event.java
```java
package eda;

// Representa um "fato do domínio" (algo que aconteceu).
// Em EDA, eventos carregam intenção e contexto do negócio.
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

// Contrato para "reagentes" a eventos.
// Cada handler implementa uma ação de negócio disparada por um tipo de evento.
public interface EventHandler {
    void handle(Event evento);
}
```

EventBus.java
```java
package eda;

import java.util.*;

// Barramento de eventos (em memória).
// Permite registrar handlers por tipo de evento e distribuir publicações.
public class EventBus {
    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    // Registra um handler para um tipo de evento específico
    public void registrar(String tipoEvento, EventHandler handler) {
        handlers.computeIfAbsent(tipoEvento, e -> new ArrayList<>()).add(handler);
        System.out.println("✅ Handler registrado para: " + tipoEvento);
    }

    // Publica um evento: todos os handlers interessados reagem
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

// Demonstração: diferentes "módulos" (handlers) reagem a eventos de domínio.
// É a essência da EDA: comunicação por eventos, componentes desacoplados.
public class MainEDA {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();

        // Módulo de Notificação
        eventBus.registrar("PedidoCriado", e ->
                System.out.println("📩 [Notificação] E-mail para cliente do " + e.getDados()));

        // Módulo de Pagamento
        eventBus.registrar("PedidoCriado", e ->
                System.out.println("💰 [Pagamento] Reservando valor do " + e.getDados()));

        // Módulo de Estoque
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🏬 [Estoque] Reduzindo estoque do " + e.getDados()));

        // Módulo de Logística
        eventBus.registrar("PagamentoConfirmado", e ->
                System.out.println("🚚 [Logística] Separando itens do " + e.getDados()));

        // Módulo de Auditoria
        eventBus.registrar("PedidoCancelado", e ->
                System.out.println("⚠️ [Auditoria] Pedido cancelado: " + e.getDados()));

        // Eventos de domínio que disparam várias reações
        eventBus.publicar(new Event("PedidoCriado", "pedido#123"));
        eventBus.publicar(new Event("PagamentoConfirmado", "pedido#123"));
        eventBus.publicar(new Event("PedidoCancelado", "pedido#987"));
    }
}
```

Resumo da comparação:
- Semelhança com Observer: múltiplos "observadores" (handlers) reagem a eventos.
- Diferença: nível de arquitetura; orientação a eventos de domínio; facilita escalabilidade, microserviços, CQRS/Event Sourcing e serverless.

---

### Diagrama comparativo UML textual — evolução conceitual

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
  - Mantém tópicos e assinantes
  - Distribui mensagens publicadas
  - Desacopla emissores e receptores (N→N)
end note
```

Reactive Programming (Java Flow API)
```
Flow.Publisher --> Flow.Subscriber
Flow.Subscriber <--> Flow.Subscription : request(n) / cancel()
note right
  - Assíncrono, non-blocking
  - Backpressure (controle de fluxo)
  - Streams contínuas e composição
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
  - Base para CQRS, Event Sourcing, Serverless
end note
```

---

### Conclusão

Tudo é Observer em diferentes camadas:

- Direto (1→N): Observer clássico (listeners, handlers locais)
- Generalizado (N→N): Pub/Sub (brokers e MQs: JMS, Kafka, RabbitMQ, SNS/SQS)
- Reativo: Flow API, RxJava, Reactor (streams assíncronas com backpressure)
- Arquitetural: EDA (Event Sourcing, CQRS, Serverless, microserviços)

Resumo da jornada:
Observer → Pub/Sub → Reactive → Event-driven architecture

A raiz conceitual é a mesma (notificação desacoplada). O que muda é o nível (objeto vs. sistema), o intermediário (broker/bus), e a natureza do fluxo (pontual vs. contínuo/reativo).