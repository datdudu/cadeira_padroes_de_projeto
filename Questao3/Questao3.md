# 3. Declínio dos Padrões Criacionais GoF vs Ascensão da Dependency Injection

## Introdução
O livro "Design Patterns: Elements of Reusable Object-Oriented Software" (GoF, 1994) é um clássico, mas 30 anos depois nem todos os 23 padrões continuam relevantes. Alguns foram absorvidos por linguagens modernas, outros considerados overengineering, e alguns praticamente caíram em desuso.

Este documento demonstra através de código como a **Dependency Injection (DI)** substituiu os padrões criacionais clássicos, oferecendo soluções mais simples, testáveis e maintíveis.

---

## Padrões Criacionais Mais Criticados

### 🚫 **Singleton**
- **Crítica:** Promove global state, dificulta testes, acoplamento forte
- **Status:** Amplamente desencorajado; substituído por Dependency Injection

### 🚫 **Abstract Factory**  
- **Crítica:** Muita verbosidade, excesso de interfaces/classes só para criação de objetos
- **Status:** Em linguagens modernas (Java com DI/Spring, C# com IoC, Python dinâmico), tornou-se raramente necessário

### 🚫 **Prototype**
- **Crítica:** Duplicação de objetos via clone() se mostrou pouco prática, especialmente em linguagens com garbage collector e construtores ricos
- **Status:** Pouco usado fora de contextos muito específicos (ex.: jogos ou DSLs)

---

## 1. Singleton vs Dependency Injection

### ❌ **Problema: Singleton Clássico**

#### Implementação Problemática:
```java
// Singleton clássico - PROBLEMÁTICO
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String connectionString;
    
    private DatabaseConnection() {
        this.connectionString = "jdbc:mysql://localhost:3306/mydb";
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public void connect() {
        System.out.println("Conectando com: " + connectionString);
    }
}

// Uso problemático
public class UserService {
    public void createUser(String name) {
        DatabaseConnection db = DatabaseConnection.getInstance(); // Acoplamento forte
        db.connect();
        System.out.println("Usuário criado: " + name);
    }
}
```

**Problemas identificados:**
- **Global State**: Estado global compartilhado
- **Testabilidade**: Impossível mockar para testes
- **Acoplamento**: Classe fortemente acoplada ao Singleton
- **Concorrência**: Problemas de thread-safety
- **Configuração**: Difícil ter múltiplas configurações

### ✅ **Solução: Dependency Injection**

#### Implementação com DI:
```java
// Interface para inversão de dependência
public interface DatabaseConnection {
    void connect();
    void disconnect();
}

// Implementação concreta
public class MySQLConnection implements DatabaseConnection {
    private String connectionString;
    
    public MySQLConnection(String connectionString) {
        this.connectionString = connectionString;
    }
    
    @Override
    public void connect() {
        System.out.println("Conectando MySQL: " + connectionString);
    }
    
    @Override
    public void disconnect() {
        System.out.println("Desconectando MySQL");
    }
}

// Serviço com dependência injetada
public class UserService {
    private final DatabaseConnection database;
    
    // Injeção via construtor
    public UserService(DatabaseConnection database) {
        this.database = database;
    }
    
    public void createUser(String name) {
        database.connect();
        System.out.println("Usuário criado: " + name);
        database.disconnect();
    }
}

// Container DI simples
public class DIContainer {
    public static UserService createUserService() {
        DatabaseConnection db = new MySQLConnection("jdbc:mysql://localhost:3306/mydb");
        return new UserService(db);
    }
    
    // Para testes
    public static UserService createUserServiceForTesting() {
        DatabaseConnection mockDb = new MockDatabaseConnection();
        return new UserService(mockDb);
    }
}
```

---

## 2. Abstract Factory vs Dependency Injection

### ❌ **Problema: Abstract Factory Verboso**

#### Implementação Problemática:
```java
// Abstract Factory - MUITO VERBOSO
public abstract class DatabaseFactory {
    public abstract Connection createConnection();
    public abstract Statement createStatement();
    public abstract ResultSet createResultSet();
}

public class MySQLFactory extends DatabaseFactory {
    @Override
    public Connection createConnection() {
        return new MySQLConnection();
    }
    
    @Override
    public Statement createStatement() {
        return new MySQLStatement();
    }
    
    @Override
    public ResultSet createResultSet() {
        return new MySQLResultSet();
    }
}

public class PostgreSQLFactory extends DatabaseFactory {
    @Override
    public Connection createConnection() {
        return new PostgreSQLConnection();
    }
    
    @Override
    public Statement createStatement() {
        return new PostgreSQLStatement();
    }
    
    @Override
    public ResultSet createResultSet() {
        return new PostgreSQLResultSet();
    }
}

// Uso complicado
public class DatabaseService {
    private DatabaseFactory factory;
    
    public DatabaseService(String dbType) {
        // Acoplamento na criação
        if ("mysql".equals(dbType)) {
            this.factory = new MySQLFactory();
        } else if ("postgresql".equals(dbType)) {
            this.factory = new PostgreSQLFactory();
        }
    }
    
    public void executeQuery() {
        Connection conn = factory.createConnection();
        Statement stmt = factory.createStatement();
        ResultSet rs = factory.createResultSet();
        // ... lógica complexa
    }
}
```

**Problemas identificados:**
- **Verbosidade**: Muitas classes e interfaces apenas para criação
- **Complexidade**: Hierarquia desnecessária de factories
- **Manutenção**: Adicionar novo tipo requer múltiplas classes
- **Testabilidade**: Difícil configurar para testes

### ✅ **Solução: Dependency Injection**

#### Implementação com DI:
```java
// Interfaces simples
public interface DatabaseConnection {
    void connect();
    void executeQuery(String sql);
}

public interface QueryProcessor {
    String processQuery(String sql);
}

public interface ResultFormatter {
    String formatResult(Object result);
}

// Implementações específicas
public class MySQLConnection implements DatabaseConnection {
    @Override
    public void connect() {
        System.out.println("MySQL conectado");
    }
    
    @Override
    public void executeQuery(String sql) {
        System.out.println("Executando no MySQL: " + sql);
    }
}

public class MySQLQueryProcessor implements QueryProcessor {
    @Override
    public String processQuery(String sql) {
        return "MySQL processou: " + sql;
    }
}

// Serviço simples com DI
public class DatabaseService {
    private final DatabaseConnection connection;
    private final QueryProcessor processor;
    private final ResultFormatter formatter;
    
    // Injeção de todas as dependências
    public DatabaseService(DatabaseConnection connection, 
                          QueryProcessor processor, 
                          ResultFormatter formatter) {
        this.connection = connection;
        this.processor = processor;
        this.formatter = formatter;
    }
    
    public String executeQuery(String sql) {
        connection.connect();
        String result = processor.processQuery(sql);
        return formatter.formatResult(result);
    }
}

// Configuração centralizada
public class DatabaseConfiguration {
    
    public static DatabaseService createMySQLService() {
        return new DatabaseService(
            new MySQLConnection(),
            new MySQLQueryProcessor(),
            new JSONResultFormatter()
        );
    }
    
    public static DatabaseService createPostgreSQLService() {
        return new DatabaseService(
            new PostgreSQLConnection(),
            new PostgreSQLQueryProcessor(),
            new XMLResultFormatter()
        );
    }
    
    // Para testes - fácil configuração
    public static DatabaseService createTestService() {
        return new DatabaseService(
            new MockConnection(),
            new MockProcessor(),
            new MockFormatter()
        );
    }
}
```

---

## 3. Prototype vs Dependency Injection

### ❌ **Problema: Prototype Complicado**

#### Implementação Problemática:
```java
// Prototype - COMPLICADO E PERIGOSO
public abstract class DatabaseConfig implements Cloneable {
    protected String host;
    protected int port;
    protected String database;
    protected Map<String, Object> properties;
    
    public abstract void connect();
    
    @Override
    public DatabaseConfig clone() {
        try {
            DatabaseConfig cloned = (DatabaseConfig) super.clone();
            // Deep copy necessário para objetos mutáveis
            cloned.properties = new HashMap<>(this.properties);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }
    
    // Getters e setters...
}

public class MySQLConfig extends DatabaseConfig {
    public MySQLConfig() {
        this.host = "localhost";
        this.port = 3306;
        this.database = "mydb";
        this.properties = new HashMap<>();
    }
    
    @Override
    public void connect() {
        System.out.println("MySQL: " + host + ":" + port + "/" + database);
    }
}

// Uso problemático
public class ConfigManager {
    private Map<String, DatabaseConfig> prototypes = new HashMap<>();
    
    public ConfigManager() {
        prototypes.put("mysql", new MySQLConfig());
        prototypes.put("postgresql", new PostgreSQLConfig());
    }
    
    public DatabaseConfig getConfig(String type) {
        DatabaseConfig prototype = prototypes.get(type);
        if (prototype != null) {
            return prototype.clone(); // Problemas de deep/shallow copy
        }
        return null;
    }
}
```

**Problemas identificados:**
- **Complexidade do Clone**: Deep vs shallow copy
- **Erro propenso**: CloneNotSupportedException
- **Mutabilidade**: Problemas com objetos mutáveis
- **Performance**: Clone pode ser custoso

### ✅ **Solução: Dependency Injection + Factory Methods**

#### Implementação com DI:
```java
// Configuração imutável
public class DatabaseConfig {
    private final String host;
    private final int port;
    private final String database;
    private final Map<String, Object> properties;
    
    // Builder pattern para configuração flexível
    public static class Builder {
        private String host = "localhost";
        private int port = 3306;
        private String database;
        private Map<String, Object> properties = new HashMap<>();
        
        public Builder host(String host) {
            this.host = host;
            return this;
        }
        
        public Builder port(int port) {
            this.port = port;
            return this;
        }
        
        public Builder database(String database) {
            this.database = database;
            return this;
        }
        
        public Builder property(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }
        
        public DatabaseConfig build() {
            return new DatabaseConfig(host, port, database, new HashMap<>(properties));
        }
    }
    
    private DatabaseConfig(String host, int port, String database, Map<String, Object> properties) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.properties = properties;
    }
    
    // Getters...
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }
    public Map<String, Object> getProperties() { return new HashMap<>(properties); }
}

// Factory com DI
public class DatabaseConfigFactory {
    
    public static DatabaseConfig createMySQLConfig() {
        return new DatabaseConfig.Builder()
            .host("localhost")
            .port(3306)
            .database("mysql_db")
            .property("charset", "utf8")
            .build();
    }
    
    public static DatabaseConfig createPostgreSQLConfig() {
        return new DatabaseConfig.Builder()
            .host("localhost")
            .port(5432)
            .database("postgres_db")
            .property("ssl", true)
            .build();
    }
    
    public static DatabaseConfig createTestConfig() {
        return new DatabaseConfig.Builder()
            .host("localhost")
            .port(0)
            .database("test_db")
            .property("memory", true)
            .build();
    }
    
    // Configuração customizada
    public static DatabaseConfig createCustomConfig(String host, int port, String db) {
        return new DatabaseConfig.Builder()
            .host(host)
            .port(port)
            .database(db)
            .build();
    }
}
```

---

## 4. Demonstração com Framework DI Moderno

### Simulando Spring-like DI Container

```java
// Anotações simples para DI
@interface Component {}
@interface Inject {}
@interface Qualifier {
    String value();
}

// Componentes com anotações
@Component
public class EmailService {
    public void sendEmail(String to, String message) {
        System.out.println("Email enviado para " + to + ": " + message);
    }
}

@Component
public class SMSService {
    public void sendSMS(String phone, String message) {
        System.out.println("SMS enviado para " + phone + ": " + message);
    }
}

@Component
public class NotificationService {
    private final EmailService emailService;
    private final SMSService smsService;
    
    // Injeção automática pelo container
    @Inject
    public NotificationService(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }
    
    public void notifyUser(String userId, String message) {
        emailService.sendEmail(userId + "@email.com", message);
        smsService.sendSMS("555-" + userId, message);
    }
}

// Container DI simples
public class SimpleDIContainer {
    private Map<Class<?>, Object> instances = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {
        if (instances.containsKey(clazz)) {
            return (T) instances.get(clazz);
        }
        
        try {
            T instance = createInstance(clazz);
            instances.put(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar instância de " + clazz.getName(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getConstructors();
        
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class) || constructor.getParameterCount() == 0) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = getInstance(paramTypes[i]);
                }
                
                return (T) constructor.newInstance(params);
            }
        }
        
        throw new RuntimeException("Nenhum construtor adequado encontrado para " + clazz.getName());
    }
}
```

---

## 5. Comparação: GoF vs DI

### Tabela Comparativa

| Aspecto | Singleton | Abstract Factory | Prototype | Dependency Injection |
|---------|-----------|------------------|-----------|---------------------|
| **Testabilidade** | ❌ Muito difícil | ❌ Complicada | ⚠️ Moderada | ✅ Excelente |
| **Flexibilidade** | ❌ Muito baixa | ⚠️ Limitada | ⚠️ Moderada | ✅ Muito alta |
| **Manutenibilidade** | ❌ Problemática | ❌ Verbosa | ⚠️ Complexa | ✅ Simples |
| **Acoplamento** | ❌ Muito alto | ⚠️ Moderado | ⚠️ Moderado | ✅ Muito baixo |
| **Configuração** | ❌ Hardcoded | ❌ Complexa | ⚠️ Manual | ✅ Centralizada |
| **Reusabilidade** | ❌ Baixa | ⚠️ Limitada | ⚠️ Moderada | ✅ Alta |

### Vantagens da Dependency Injection

#### ✅ **1. Testabilidade Superior**
```java
// Fácil de testar com mocks
@Test
public void testUserService() {
    DatabaseConnection mockDb = mock(DatabaseConnection.class);
    UserService service = new UserService(mockDb);
    
    service.createUser("João");
    
    verify(mockDb).connect();
    verify(mockDb).disconnect();
}
```

#### ✅ **2. Configuração Flexível**
```java
// Diferentes configurações para diferentes ambientes
public class AppConfiguration {
    
    @Profile("development")
    public DatabaseConnection devDatabase() {
        return new MySQLConnection("localhost:3306/dev_db");
    }
    
    @Profile("production")
    public DatabaseConnection prodDatabase() {
        return new MySQLConnection("prod-server:3306/prod_db");
    }
    
    @Profile("test")
    public DatabaseConnection testDatabase() {
        return new H2Connection(":memory:");
    }
}
```

#### ✅ **3. Inversão de Controle**
```java
// O container gerencia o ciclo de vida
public class ModernApplication {
    public static void main(String[] args) {
        SimpleDIContainer container = new SimpleDIContainer();
        
        // Container resolve todas as dependências automaticamente
        NotificationService service = container.getInstance(NotificationService.class);
        service.notifyUser("12345", "Bem-vindo ao sistema!");
    }
}
```

---

## Conclusão

### 🎯 **Por que DI Venceu os Padrões Criacionais GoF?**

1. **Simplicidade**: Menos código boilerplate
2. **Testabilidade**: Injeção de mocks e stubs
3. **Flexibilidade**: Configuração externa e profiles
4. **Manutenibilidade**: Baixo acoplamento
5. **Frameworks**: Suporte nativo em Spring, CDI, Guice, etc.

### 📈 **Evolução dos Paradigmas**

- **1994 (GoF)**: Padrões para problemas de OOP
- **2000s**: Emergência de frameworks DI
- **2010s**: DI se torna padrão na indústria
- **Hoje**: Padrões criacionais GoF considerados anti-patterns

### 🚀 **Frameworks Modernos que Substituíram GoF**

- **Java**: Spring Framework, CDI, Guice
- **C#**: .NET Core DI, Autofac, Unity
- **Python**: Django DI, FastAPI Depends
- **TypeScript**: NestJS, InversifyJS
- **Kotlin**: Koin, Kodein

A **Dependency Injection** não apenas substituiu os padrões criacionais do GoF, mas ofereceu uma solução mais elegante, testável e maintível para o gerenciamento de dependências em aplicações modernas.