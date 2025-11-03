#  Padrões de Projeto - Apresentação para Canva

---

## 🎯 Slide 1 - Título
### **Padrões de Projeto: Da Teoria à Prática**
**Explorando DAO, Data Mapper, Repository, Refatorações e a Evolução dos Padrões**

---

## 📋 Slide 2 - Agenda
### **O que vamos abordar hoje:**

1. **🗃️ Padrões de Persistência** - DAO, Data Mapper e Repository
2. **🔄 Refatorações Clássicas** - Strategy e Decorator
3. **💉 Evolução dos Padrões** - DI vs GOF Criacionais
4. **👁️ Observer em Todo Lugar** - Do Clássico ao Reativo

---

## 🗃️ Slide 3 - Padrões de Persistência - Introdução
### **Três Abordagens para Acessar Dados**

**DAO** → **Data Mapper** → **Repository**

🎯 **Objetivo:** Separar lógica de negócio da persistência de dados

---

## 🗃️ Slide 4 - DAO (Data Access Object)
### **Características Principais:**

✅ **Abstrai** acesso à fonte de dados  
❌ **Não separa** domínio da persistência  
⚙️ **Foca** em operações CRUD básicas  

### **Quando usar:**
- Aplicações simples
- Mapeamento objeto-relacional direto
- Prototipagem rápida

---

## 🗃️ Slide 5 - Data Mapper
### **Características Principais:**

✅ **Separa totalmente** domínio e persistência  
✅ **Objetos puros** de domínio (sem setters)  
⚙️ **Mapeia** entre camadas  

### **Vantagens:**
- Domínio livre de SQL
- Maior testabilidade
- Flexibilidade na modelagem

---

## 🗃️ Slide 6 - Repository
### **Características Principais:**

✅ **API de alto nível** para o domínio  
✅ **Trabalha com agregados** completos  
✅ **Mantém invariantes** de negócio  

### **Diferencial:**
- Pensa em **"coleções"** de objetos
- Gerencia **agregados inteiros**
- Preserva **regras de negócio**

---

## 🗃️ Slide 7 - Agregados e Invariantes
### **Conceitos Fundamentais:**

**🧩 Agregado:** Objeto de domínio + suas dependências  
**🔒 Invariante:** Regras que sempre devem ser verdadeiras  

### **Exemplo:**
```
Pedido (raiz) + ItemsPedido (dependentes) = AGREGADO
```

**Invariante:** "Pedido só confirma com pelo menos 1 item"

---

## 🗃️ Slide 8 - Comparação Visual
### **DAO vs Data Mapper vs Repository**

| Aspecto | DAO | Data Mapper | Repository |
|---------|-----|-------------|------------|
| **Separação** | ❌ Baixa | ✅ Total | ✅ Total |
| **Complexidade** | 🟢 Simples | 🟡 Média | 🟡 Média |
| **Flexibilidade** | 🟡 Limitada | ✅ Alta | ✅ Alta |
| **Foco** | Tabelas | Mapeamento | Agregados |

---

## 🔄 Slide 9 - Refatorações Clássicas
### **"Refactoring to Patterns" - Joshua Kerievsky**

**🎯 Objetivo:** Transformar código legado em padrões limpos

### **Duas Refatorações Principais:**
1. **Replace Conditional with Polymorphism** → **Strategy**
2. **Move Embellishment to Decorator** → **Decorator**

---

## 🔄 Slide 10 - Strategy Pattern
### **Problema:** Múltiplos if/else para comportamentos

```java
if (tipo.equals("VIP")) {
    return valor * 0.9; // 10% desconto
} else if (tipo.equals("PREMIUM")) {
    return valor * 0.8; // 20% desconto
}
```

### **Solução:** Uma interface, múltiplas implementações
```java
interface EstrategiaDesconto {
    double calcular(double valor);
}
```

---

## 🔄 Slide 11 - Decorator Pattern
### **Problema:** Classe com muitas responsabilidades opcionais

```java
boolean incluirCabecalho;
boolean incluirRodape;
boolean comprimirDados;
boolean criptografarDados;
```

### **Solução:** Decorators compostos dinamicamente
```java
new CriptografiaDecorator(
    new CabecalhoDecorator(
        new RelatorioBasico(dados)
    )
);
```

---

## 🔄 Slide 12 - Benefícios das Refatorações
### **Strategy Pattern:**
✅ **Extensibilidade** - Novos tipos sem modificar código  
✅ **Testabilidade** - Cada estratégia isolada  
✅ **Manutenibilidade** - Responsabilidade única  

### **Decorator Pattern:**
✅ **Flexibilidade** - Combinações ilimitadas  
✅ **Composição** - Funcionalidades em runtime  
✅ **Modularidade** - Cada decorator independente  

---

## 💉 Slide 13 - Evolução: GOF vs Dependency Injection
### **30 anos depois... o que mudou?**

** 1994 - GoF:** 23 padrões criacionais  
**🚀 2024 - Hoje:** Dependency Injection domina  

### **Padrões "em declínio":**
- 🚫 **Singleton** → Global state problemático
- 🚫 **Abstract Factory** → Verbosidade excessiva  
- 🚫 **Prototype** → Clone() pouco prático

---

## 💉 Slide 14 - Singleton vs DI
### **❌ Singleton Problemático:**
```java
DatabaseConnection.getInstance() // Acoplamento forte
```

### **✅ Dependency Injection:**
```java
public UserService(DatabaseConnection db) {
    this.database = db; // Injeção no construtor
}
```

**Vantagens DI:** Testável, flexível, configurável

---

## 💉 Slide 15 - Por que DI Venceu?
### **Principais Motivos:**

1. **🧪 Testabilidade** - Fácil injeção de mocks
2. **🔧 Flexibilidade** - Configuração externa
3. **🏗️ Frameworks** - Spring, CDI, Guice
4. **📝 Simplicidade** - Menos código boilerplate
5. **🔗 Baixo Acoplamento** - Inversão de dependências

---

## 👁️ Slide 16 - Observer: O Padrão Onipresente
### **Do Local ao Distribuído**

**Observer Clássico** → **Pub/Sub** → **Reactive** → **Event-Driven**

🎯 **Essência:** Um emissor notifica múltiplos receptores sobre mudanças

---

## 👁️ Slide 17 - Observer Clássico
### **Características:**
✅ **Comunicação direta** (1→N)  
✅ **Síncrono** e local  
✅ **Acoplamento baixo** entre sujeito/observador  

### **Exemplos:**
- Listeners de GUI (JButton)
- Eventos JavaFX/Swing
- Model-View patterns

---

## 👁️ Slide 18 - Publish-Subscribe (Pub/Sub)
### **Evolução com Broker:**
✅ **Intermediário** desacopla emissor/receptor  
✅ **Múltiplos produtores** e consumidores (N→N)  
✅ **Roteamento** por tópicos  

### **Tecnologias:**
- Apache Kafka, RabbitMQ
- AWS SNS/SQS, Google Pub/Sub
- MQTT (IoT)

---

## 👁️ Slide 19 - Reactive Programming
### **Observer + Streams Assíncronas:**
✅ **Backpressure** - Controle de fluxo  
✅ **Non-blocking** - Threads não bloqueiam  
✅ **Composição** - Pipeline de operações  

### **Tecnologias:**
- Java Flow API, RxJava
- Project Reactor (Spring WebFlux)
- Kotlin Flow

---

## 👁️ Slide 20 - Event-Driven Architecture (EDA)
### **Observer no Nível Arquitetural:**
✅ **Eventos de domínio** orquestram sistemas  
✅ **Microserviços** desacoplados  
✅ **Escalabilidade** distribuída  

### **Padrões Relacionados:**
- Event Sourcing
- CQRS
- Serverless (AWS Lambda)

---

## 👁️ Slide 21 - Comparação Visual Observer
### **Evolução Conceitual:**

| Tipo | Comunicação | Escopo | Exemplos |
|------|-------------|---------|----------|
| **Clássico** | Direta | Local | GUI Listeners |
| **Pub/Sub** | Via Broker | Sistema | Kafka, RabbitMQ |
| **Reactive** | Streams | Assíncrono | RxJava, WebFlux |
| **EDA** | Eventos | Distribuído | Microserviços |

---

## 📊 Slide 22 - Resumo Executivo
### **Principais Lições:**

1. **🗃️ Persistência:** Repository > Data Mapper > DAO
2. **🔄 Refatoração:** Patterns melhoram código legado
3. **💉 DI:** Substituiu padrões criacionais GOF
4. **👁️ Observer:** Base de toda comunicação reativa moderna

---

## 🎯 Slide 23 - Aplicação Prática
### **Como aplicar no dia a dia:**

**🏢 Em Projetos Enterprise:**
- Use Repository para domínios complexos
- Aplique Strategy para regras de negócio variáveis
- Implemente DI com Spring/CDI

**⚡ Em Sistemas Reativos:**
- Java Flow API para streams
- Event-driven para microserviços

---

## 🚀 Slide 24 - Próximos Passos
### **Aprofundamento Recomendado:**

 **Livros:**
- "Refactoring to Patterns" - Joshua Kerievsky
- "Patterns of Enterprise Application Architecture" - Martin Fowler

 **Prática:**
- Implemente os 4 padrões de Observer
- Refatore código legado com Strategy/Decorator
- Configure DI Container (Spring Boot)

---

## 🎉 Slide 25 - Conclusão
### **Padrões são Ferramentas**

**✅ Use quando apropriado**  
**❌ Evite over-engineering**  
**🎯 Foque na simplicidade**  

### **A evolução continua...**
**Observer (1994) → Reactive Streams (2024)**

---

## 📞 Slide 26 - Perguntas?
### **Discussão e Dúvidas**

**💬 Tópicos para discussão:**
- Experiências com padrões em projetos reais
- Desafios na implementação
- Escolha entre DAO/Repository
- Casos de uso para cada tipo de Observer

---

# 📝 Guia para Uso no Canva

## Dicas de Design:

1. **🎨 Cores:** Use uma paleta consistente (azul, verde, laranja para destaque)
2. **📊 Ícones:** Emojis ajudam na visualização rápida
3. **📈 Diagramas:** Transforme tabelas em infográficos visuais
4. **💡 Código:** Use blocos destacados para snippets
5. **🔄 Transições:** Crie slides de transição entre seções

## Elementos Visuais Sugeridos:

- **Diagramas UML** simplificados para os padrões
- **Fluxogramas** para mostrar evolução (Observer → Pub/Sub → Reactive)
- **Antes/Depois** para refatorações
- **Comparação visual** em tabelas coloridas
- **Timeline** da evolução dos padrões (1994 → 2024)

## Adaptações para o Canva:

- Quebrar slides longos em múltiplos slides
- Usar animations para reveal progressivo de informações
- Adicionar exemplos visuais (diagramas, flowcharts)
- Incluir quotes destacadas dos livros citados
- Criar slides de "checkpoint" para resumir seções
