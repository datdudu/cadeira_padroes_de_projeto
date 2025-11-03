#  Guia de Estruturação - Slides Baseados nas 4 Questões

## 🎯 TÍTULO DA APRESENTAÇÃO
**"Padrões de Projeto: Das Questões Teóricas à Aplicação Prática"**

---

## 📋 SLIDE 1 - TÍTULO + AGENDA
### Conteúdo:
- **Título:** "Padrões de Projeto: Das Questões Teóricas à Aplicação Prática"
- **Agenda das 4 Questões:**
  1. 🗃️ **Questão 1:** Padrões de Persistência (DAO, Data Mapper, Repository)
  2. 🔄 **Questão 2:** Refatorações (Strategy e Decorator)
  3. 💉 **Questão 3:** Declínio GOF vs Ascensão DI
  4. 👁️ **Questão 4:** Observer Onipresente

### Design:
- Fundo azul escuro
- Título grande e centralizado
- Agenda com ícones coloridos
- Subtítulo com seu nome/disciplina

---

## 🗃️ QUESTÃO 1: PADRÕES DE PERSISTÊNCIA (5 slides)

### SLIDE 2 - INTRODUÇÃO QUESTÃO 1
**Título:** "Questão 1 - Em relação aos padrões de persistência: DAO, Data Mapper e Repository"

**Conteúdo:**
- Subtítulo: "Compare-os no nível conceitual"
- **Objetivo:** Separar lógica de negócio da persistência de dados
- **Evolução:** DAO → Data Mapper → Repository
- Ícone: 🗃️

### SLIDE 3 - DAO (DATA ACCESS OBJECT)
**Título:** "DAO - Data Access Object"

**Conteúdo:**
- **Conceito:** "O data access object foca em abstrair toda a parte de acesso a fonte de dados (persistência), fazendo com que a implementação do acesso a fonte de dados possa ser alterado sem modificar o código do cliente"
- **Característica Principal:** O domínio NÃO é separado da lógica de acesso a dados
- **Foco:** Operações CRUD básicas

**Código para incluir:**
```java
public interface LivroDAO {
    void salvar(Livro livro);
    void atualizar(Livro livro);
    void deletar(Long id);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarTodos();
}
```

### SLIDE 4 - DATA MAPPER
**Título:** "Data Mapper"

**Conteúdo:**
- **Conceito:** "Tem o propósito de separar totalmente a lógica de domínio e a persistência de dados"
- **Função:** Faz o intermédio (mapeamento do objeto de domínio para o objeto de persistência)
- **Vantagem:** Pode ser usado junto ao DAO

**Código para incluir:**
```java
public class Livro {
    private final String titulo;
    private final String autor;
    
    // Construtor (sem setters)
    public Livro(String titulo, String autor...) {...}
}

private Livro mapRow(ResultSet rs) {
    return new Livro(
        rs.getString("titulo"),
        rs.getString("autor")...
    );
}
```

### SLIDE 5 - REPOSITORY
**Título:** "Repository"

**Conteúdo:**
- **Conceito:** "Esconde completamente a lógica de persistência de dados"
- **Diferencial:** Atua como uma API de alto nível para consulta e manipulação de agregados
- **Foco:** Objetos do domínio como "coleções"

**Código para incluir:**
```java
public interface LivroRepository {
    void salvar(Livro livro);
    void remover(Livro livro);
    List<Livro> buscarDisponiveis();
    List<Livro> buscarPorAutor(String autor);
}
```

### SLIDE 6 - AGREGADOS E INVARIANTES
**Título:** "Agregados e Invariantes"

**Conteúdo:**
- **Agregado:** "Objeto de domínio + suas dependências"
- **Invariante:** "Regras que sempre devem ser verdadeiras"
- **Exemplo:** Pedido (raiz) + ItemsPedido (dependentes) = AGREGADO
- **Invariante exemplo:** "Pedido só confirma com pelo menos 1 item"

**Código para incluir:**
```java
public void adicionarItem(String produtoId, int quantidade, BigDecimal preco) {
    if (status != StatusPedido.RASCUNHO) {
        throw new IllegalStateException("Não é possível adicionar itens");
    }
    // Invariante mantida
}
```

---

## 🔄 QUESTÃO 2: REFATORAÇÕES (4 slides)

### SLIDE 7 - INTRODUÇÃO QUESTÃO 2
**Título:** "Questão 2 - Refatorações do Livro 'Refactoring to Patterns'"

**Conteúdo:**
- **Autor:** Joshua Kerievsky (2004)
- **Objetivo:** Transformar código legado em padrões limpos
- **Duas refatorações principais:**
  1. Replace Conditional with Polymorphism → Strategy
  2. Move Embellishment to Decorator → Decorator

### SLIDE 8 - STRATEGY PATTERN
**Título:** "Replace Conditional with Polymorphism → Strategy"

**Conteúdo:**
- **Problema:** "Quando temos uma classe com muitos condicionais que executam comportamentos diferentes baseados em tipos ou estados, o código se torna difícil de manter e estender"

**ANTES (lado esquerdo):**
```java
if (tipo.equals("VIP")) {
    return valor * 0.9; // 10% desconto
} else if (tipo.equals("PREMIUM")) {
    return valor * 0.8; // 20% desconto
}
```

**DEPOIS (lado direito):**
```java
public interface EstrategiaDesconto {
    double calcular(double valor);
}

public class DescontoVIP implements EstrategiaDesconto {
    public double calcular(double valor) {
        return valor * 0.9;
    }
}
```

### SLIDE 9 - DECORATOR PATTERN
**Título:** "Move Embellishment to Decorator"

**Conteúdo:**
- **Problema:** "Quando uma classe acumula muitas funcionalidades opcionais ou 'enfeites', ela se torna complexa e difícil de manter"

**ANTES (lado esquerdo):**
```java
boolean incluirCabecalho;
boolean incluirRodape;
boolean comprimirDados;
boolean criptografarDados;
```

**DEPOIS (lado direito):**
```java
new CriptografiaDecorator(
    new CabecalhoDecorator(
        new RelatorioBasico(dados)
    )
);
```

### SLIDE 10 - COMPARAÇÃO DECORATOR/STREAMS/BUILDER
**Título:** "Semelhanças: Decorator vs Java Streams vs Builder"

**Conteúdo:**
- **Conceito comum:** Composição fluente
- **Decorator:** Aninhamento de objetos
- **Streams:** Pipeline de operações
- **Builder:** Métodos fluentes

**Código exemplo:**
```java
// Decorator Fluente
new RelatorioFluenteBuilder(dados)
    .comCabecalho()
    .comTimestamp()
    .comCriptografia()
    .gerar();
```

---

## 💉 QUESTÃO 3: DECLÍNIO GOF vs ASCENSÃO DI (5 slides)

### SLIDE 11 - INTRODUÇÃO QUESTÃO 3
**Título:** "Questão 3 - Declínio dos Padrões Criacionais GoF vs Ascensão da Dependency Injection"

**Conteúdo:**
- **GoF (1994):** 23 padrões criacionais
- **Hoje (2024):** Dependency Injection domina
- **Realidade:** "30 anos depois nem todos os padrões continuam relevantes"

### SLIDE 12 - PADRÕES EM DECLÍNIO
**Título:** "Padrões Criacionais Criticados"

**Conteúdo:**
- **Singleton:** Global state problemático, testabilidade impossível
- **Abstract Factory:** Verbosidade excessiva, overengineering
- **Prototype:** Clone() pouco prático, problemas de deep/shallow copy
- **Motivo:** "Alguns foram absorvidos por linguagens modernas, outros considerados overengineering"

### SLIDE 13 - SINGLETON vs DI
**Título:** "Singleton vs Dependency Injection"

**PROBLEMÁTICO (lado esquerdo):**
```java
// Singleton - PROBLEMÁTICO
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}

// Uso problemático
DatabaseConnection db = DatabaseConnection.getInstance();
```

**SOLUÇÃO DI (lado direito):**
```java
// Dependency Injection - SOLUÇÃO
public class UserService {
    private final DatabaseConnection database;
    
    public UserService(DatabaseConnection database) {
        this.database = database; // Injeção no construtor
    }
}
```

**Problemas Singleton:** Global State, Testabilidade, Acoplamento, Concorrência

### SLIDE 14 - ABSTRACT FACTORY vs DI
**Título:** "Abstract Factory vs Dependency Injection"

**PROBLEMÁTICO (lado esquerdo):**
```java
// Abstract Factory - VERBOSO
public abstract class DatabaseFactory {
    public abstract Connection createConnection();
    public abstract Statement createStatement();
}

public class MySQLFactory extends DatabaseFactory {
    public Connection createConnection() { ... }
    public Statement createStatement() { ... }
}
```

**SOLUÇÃO DI (lado direito):**
```java
// DI - SIMPLES
public class DatabaseService {
    private final DatabaseConnection connection;
    
    public DatabaseService(DatabaseConnection connection) {
        this.connection = connection;
    }
}

// Configuração externa
DatabaseService service = new DatabaseService(
    new MySQLConnection()
);
```

### SLIDE 15 - POR QUE DI VENCEU
**Título:** "Por que Dependency Injection Venceu?"

**Vantagens DI:**
1. **🧪 Testabilidade** - Fácil injeção de mocks
2. **🔧 Flexibilidade** - Configuração externa
3. **🏗️ Frameworks** - Spring, CDI, Guice
4. **📝 Simplicidade** - Menos código boilerplate  
5. **🔗 Baixo Acoplamento** - Inversão de dependências

**Frameworks que substituíram GOF:**
- Java: Spring Framework, CDI, Guice
- C#: .NET Core DI, Autofac
- Python: Django DI, FastAPI

---

## 👁️ QUESTÃO 4: OBSERVER ONIPRESENTE (5 slides)

### SLIDE 16 - INTRODUÇÃO QUESTÃO 4
**Título:** "Questão 4 - O padrão Observer é onipresente"

**Conteúdo:**
- **Essência:** "Define uma forma desacoplada de notificar múltiplos objetos sobre mudanças de estado"
- **Evolução:** Observer Clássico → Pub/Sub → Reactive → Event-Driven
- **Aplicação:** "Base para diversas tecnologias modernas"

### SLIDE 17 - OBSERVER CLÁSSICO
**Título:** "Observer Clássico - Direto 1→N"

**Conteúdo:**
- **Características:** Comunicação direta, normalmente síncrona, local
- **Exemplos:** Listeners de GUI (JButton), JavaFX/Swing, Model-View patterns

**Código:**
```java
// Observer Clássico
public interface Observador {
    void atualizar(String mensagem);
}

public class SujeitoConcreto {
    private List<Observador> observadores = new ArrayList<>();
    
    public void registrar(Observador o) { observadores.add(o); }
    public void notificar(String msg) {
        for (Observador o : observadores) {
            o.atualizar(msg);
        }
    }
}
```

### SLIDE 18 - PUBLISH-SUBSCRIBE
**Título:** "Publish-Subscribe - N→N via Broker"

**Conteúdo:**
- **Evolução:** "É uma generalização do Observer"
- **Diferença:** "Em vez do sujeito notificar diretamente os observadores, existe um intermediário (broker)"
- **Vantagem:** Desacopla completamente emissores e receptores

**Código:**
```java
// Pub/Sub com Broker
public class Broker {
    private Map<String, List<Subscriber>> topicos;
    
    public void subscribe(String topico, Subscriber sub) { ... }
    public void publish(String topico, String mensagem) { ... }
}
```

**Tecnologias:** Apache Kafka, RabbitMQ, AWS SNS/SQS, Google Pub/Sub, MQTT

### SLIDE 19 - REACTIVE PROGRAMMING
**Título:** "Reactive Programming - Streams Assíncronas"

**Conteúdo:**
- **Conceito:** "O Observer é elevado à base de toda a programação"
- **Características:** Dados fluem de forma contínua e assíncrona, objetos reagem
- **Diferencial:** Controle de fluxo (backpressure), non-blocking

**Código:**
```java
// Java Flow API
public class MessageSubscriber implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;
    
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // backpressure
    }
    
    public void onNext(String item) {
        System.out.println("Recebeu: " + item);
        subscription.request(1);
    }
}
```

**Tecnologias:** RxJava, Project Reactor, Spring WebFlux, Kotlin Flow

### SLIDE 20 - EVENT-DRIVEN ARCHITECTURE
**Título:** "Event-Driven Architecture - Nível Arquitetural"

**Conteúdo:**
- **Conceito:** "O Observer é expandido para um nível arquitetural"
- **Aplicação:** "Sistemas inteiros produzem e consomem eventos de domínio"
- **Uso:** Microserviços desacoplados, escalabilidade distribuída

**Código:**
```java
// EDA
public class EventBus {
    private Map<String, List<EventHandler>> handlers;
    
    public void registrar(String tipo, EventHandler handler) { ... }
    public void publicar(Event evento) { ... }
}

// Uso
eventBus.registrar("PedidoCriado", e -> notificarCliente(e));
eventBus.publicar(new Event("PedidoCriado", "pedido#123"));
```

**Base para:** Event Sourcing, CQRS, Serverless (AWS Lambda)

---

## 📊 SLIDE 21 - SÍNTESE FINAL
**Título:** "Síntese das 4 Questões - Evolução dos Padrões"

**Conteúdo:**
### **Principais Lições:**
1. **🗃️ Persistência:** Repository > Data Mapper > DAO (agregados e invariantes)
2. **🔄 Refatoração:** Strategy elimina if/else, Decorator compõe funcionalidades
3. **💉 Evolução:** DI substituiu Singleton/Factory (testável, flexível)
4. **👁️ Observer:** Do local ao distribuído (Clássico → Pub/Sub → Reactive → EDA)

### **Timeline Evolutiva:**
```
1994 (GoF) → 2004 (Refactoring) → 2024 (DI + Reactive)
Observer → Pub/Sub → Reactive Streams → Event-Driven
```

---

## 🎨 DICAS DE DESIGN PARA TODOS OS SLIDES:

### **Paleta de Cores:**
- **Azul:** #2E86AB (títulos e destaque)
- **Verde:** #A23B72 (benefícios e soluções)
- **Laranja:** #F18F01 (problemas e alertas)
- **Cinza:** #C73E1D (código e texto)

### **Ícones por Questão:**
- 🗃️ Persistência (DAO/Repository)
- 🔄 Refatoração (Strategy/Decorator)  
- 💉 DI vs GOF (Injeção de dependência)
- 👁️ Observer (Observação/Eventos)

### **Layout dos Slides:**
- **Título:** Grande, azul, no topo
- **Código:** Fundo cinza claro, fonte monoespaçada
- **ANTES/DEPOIS:** Dividir slide ao meio verticalmente
- **Listas:** Bullets com ícones coloridos
- **Tabelas:** Headers azuis, linhas alternadas

### **Elementos Visuais:**
- Diagramas UML simples
- Setas para mostrar evolução (→)
- Caixas destacadas para conceitos importantes
- Códigos em blocos bem delimitados