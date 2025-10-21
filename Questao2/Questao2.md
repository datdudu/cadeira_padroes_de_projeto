# 2. Refatorações do Livro "Refactoring to Patterns" (Joshua Kerievsky, 2004)

## Introdução
O Livro Refactoring to Patterns não introduz novos padrões, mas mostra como transformar código legado ou mal estruturado em código mais limpo usando padrões já existentes. Este documento ilustra duas refatorações importantes:

1. **Replace Conditional with Polymorphism** → **Strategy Pattern**
2. **Move Embellishment to Decorator** → **Decorator Pattern**

---

## 1. Replace Conditional with Polymorphism → Strategy Pattern

### Problema: Código cheio de if/else
Quando temos uma classe com muitos condicionais que executam comportamentos diferentes baseados em tipos ou estados, o código se torna difícil de manter e estender.

#### Código Antes da Refatoração:
```java
public class CalculadoraDesconto {
    public double calcular(double valor, String tipoCliente) {
        if (tipoCliente.equals("REGULAR")) {
            return valor; // Sem desconto
        } else if (tipoCliente.equals("VIP")) {
            return valor * 0.9; // 10% desconto
        } else if (tipoCliente.equals("PREMIUM")) {
            return valor * 0.8; // 20% desconto
        } else if (tipoCliente.equals("FUNCIONARIO")) {
            return valor * 0.7; // 30% desconto
        } else {
            throw new IllegalArgumentException("Tipo de cliente inválido");
        }
    }
    
    public String obterMensagem(String tipoCliente) {
        if (tipoCliente.equals("REGULAR")) {
            return "Cliente regular - sem desconto especial";
        } else if (tipoCliente.equals("VIP")) {
            return "Cliente VIP - desconto de 10%";
        } else if (tipoCliente.equals("PREMIUM")) {
            return "Cliente Premium - desconto de 20%";
        } else if (tipoCliente.equals("FUNCIONARIO")) {
            return "Funcionário - desconto de 30%";
        } else {
            throw new IllegalArgumentException("Tipo de cliente inválido");
        }
    }
}
```

**Problemas identificados:**
- Violação do princípio Open/Closed (aberto para extensão, fechado para modificação)
- Duplicação de lógica condicional
- Dificulta a adição de novos tipos de cliente
- Código difícil de testar individualmente

#### Refatoração Aplicada: Replace Conditional with Polymorphism
Substituímos os condicionais por uma hierarquia de classes que implementam o comportamento específico de cada tipo.

#### Código Após a Refatoração (Strategy Pattern):
```java
// Strategy interface
public interface EstrategiaDesconto {
    double calcularDesconto(double valor);
    String obterMensagem();
}

// Concrete Strategies
public class DescontoRegular implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor; // Sem desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Cliente regular - sem desconto especial";
    }
}

public class DescontoVIP implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.9; // 10% desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Cliente VIP - desconto de 10%";
    }
}

public class DescontoPremium implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.8; // 20% desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Cliente Premium - desconto de 20%";
    }
}

public class DescontoFuncionario implements EstrategiaDesconto {
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.7; // 30% desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Funcionário - desconto de 30%";
    }
}

// Context
public class CalculadoraDesconto {
    private EstrategiaDesconto estrategia;
    
    public CalculadoraDesconto(EstrategiaDesconto estrategia) {
        this.estrategia = estrategia;
    }
    
    public void setEstrategia(EstrategiaDesconto estrategia) {
        this.estrategia = estrategia;
    }
    
    public double calcular(double valor) {
        return estrategia.calcularDesconto(valor);
    }
    
    public String obterMensagem() {
        return estrategia.obterMensagem();
    }
}

// Factory para criar as estratégias
public class EstrategiaDescontoFactory {
    public static EstrategiaDesconto criar(String tipoCliente) {
        switch (tipoCliente.toUpperCase()) {
            case "REGULAR":
                return new DescontoRegular();
            case "VIP":
                return new DescontoVIP();
            case "PREMIUM":
                return new DescontoPremium();
            case "FUNCIONARIO":
                return new DescontoFuncionario();
            default:
                throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
        }
    }
}
```

**Benefícios da refatoração:**
- **Extensibilidade**: Novos tipos de desconto podem ser adicionados sem modificar código existente
- **Testabilidade**: Cada estratégia pode ser testada independentemente
- **Manutenibilidade**: Lógica de cada tipo está encapsulada em sua própria classe
- **Princípio da Responsabilidade Única**: Cada classe tem uma única razão para mudar

---

## 2. Move Embellishment to Decorator → Decorator Pattern

### Problema: Classe cheia de responsabilidades extras
Quando uma classe acumula muitas funcionalidades opcionais ou "enfeites", ela se torna complexa e difícil de manter.

#### Código Antes da Refatoração:
```java
public class RelatorioVendas {
    private List<String> dados;
    private boolean incluirCabecalho;
    private boolean incluirRodape;
    private boolean incluirTimestamp;
    private boolean incluirAssinatura;
    private boolean comprimirDados;
    private boolean criptografarDados;
    
    public RelatorioVendas(List<String> dados) {
        this.dados = dados;
        this.incluirCabecalho = false;
        this.incluirRodape = false;
        this.incluirTimestamp = false;
        this.incluirAssinatura = false;
        this.comprimirDados = false;
        this.criptografarDados = false;
    }
    
    // Métodos para configurar opções
    public void setIncluirCabecalho(boolean incluir) { this.incluirCabecalho = incluir; }
    public void setIncluirRodape(boolean incluir) { this.incluirRodape = incluir; }
    public void setIncluirTimestamp(boolean incluir) { this.incluirTimestamp = incluir; }
    public void setIncluirAssinatura(boolean incluir) { this.incluirAssinatura = incluir; }
    public void setComprimirDados(boolean comprimir) { this.comprimirDados = comprimir; }
    public void setCriptografarDados(boolean criptografar) { this.criptografarDados = criptografar; }
    
    public String gerar() {
        StringBuilder relatorio = new StringBuilder();
        
        // Lógica complexa com múltiplas responsabilidades
        if (incluirCabecalho) {
            relatorio.append("=== RELATÓRIO DE VENDAS ===\n");
        }
        
        if (incluirTimestamp) {
            relatorio.append("Data/Hora: ").append(java.time.LocalDateTime.now()).append("\n");
        }
        
        // Dados principais
        for (String linha : dados) {
            relatorio.append(linha).append("\n");
        }
        
        if (incluirAssinatura) {
            relatorio.append("\nAssinado digitalmente por: Sistema de Vendas v1.0\n");
        }
        
        if (incluirRodape) {
            relatorio.append("=== FIM DO RELATÓRIO ===\n");
        }
        
        String resultado = relatorio.toString();
        
        // Processamentos adicionais
        if (comprimirDados) {
            resultado = comprimir(resultado);
        }
        
        if (criptografarDados) {
            resultado = criptografar(resultado);
        }
        
        return resultado;
    }
    
    private String comprimir(String dados) {
        // Simulação de compressão
        return "[COMPRIMIDO]" + dados + "[/COMPRIMIDO]";
    }
    
    private String criptografar(String dados) {
        // Simulação de criptografia
        return "[CRIPTOGRAFADO]" + dados + "[/CRIPTOGRAFADO]";
    }
}
```

**Problemas identificados:**
- Muitas responsabilidades em uma única classe
- Difícil combinação flexível de funcionalidades
- Código complexo e difícil de testar
- Violação do princípio da responsabilidade única

#### Refatoração Aplicada: Move Embellishment to Decorator
Movemos cada "enfeite" ou funcionalidade adicional para decorators separados.

#### Código Após a Refatoração (Decorator Pattern):
```java
// Component interface
public interface Relatorio {
    String gerar();
}

// Concrete Component
public class RelatorioBasico implements Relatorio {
    private List<String> dados;
    
    public RelatorioBasico(List<String> dados) {
        this.dados = dados;
    }
    
    @Override
    public String gerar() {
        StringBuilder relatorio = new StringBuilder();
        for (String linha : dados) {
            relatorio.append(linha).append("\n");
        }
        return relatorio.toString();
    }
}

// Base Decorator
public abstract class RelatorioDecorator implements Relatorio {
    protected Relatorio relatorio;
    
    public RelatorioDecorator(Relatorio relatorio) {
        this.relatorio = relatorio;
    }
    
    @Override
    public String gerar() {
        return relatorio.gerar();
    }
}

// Concrete Decorators
public class CabecalhoDecorator extends RelatorioDecorator {
    public CabecalhoDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        return "=== RELATÓRIO DE VENDAS ===\n" + super.gerar();
    }
}

public class RodapeDecorator extends RelatorioDecorator {
    public RodapeDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        return super.gerar() + "=== FIM DO RELATÓRIO ===\n";
    }
}

public class TimestampDecorator extends RelatorioDecorator {
    public TimestampDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        String timestamp = "Data/Hora: " + java.time.LocalDateTime.now() + "\n";
        return timestamp + super.gerar();
    }
}

public class AssinaturaDecorator extends RelatorioDecorator {
    public AssinaturaDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        return super.gerar() + "\nAssinado digitalmente por: Sistema de Vendas v1.0\n";
    }
}

public class CompressaoDecorator extends RelatorioDecorator {
    public CompressaoDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        String dados = super.gerar();
        return "[COMPRIMIDO]" + dados + "[/COMPRIMIDO]";
    }
}

public class CriptografiaDecorator extends RelatorioDecorator {
    public CriptografiaDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        String dados = super.gerar();
        return "[CRIPTOGRAFADO]" + dados + "[/CRIPTOGRAFADO]";
    }
}
```

**Benefícios da refatoração:**
- **Responsabilidade Única**: Cada decorator tem uma única funcionalidade
- **Flexibilidade**: Combinações ilimitadas de funcionalidades
- **Extensibilidade**: Novos decorators podem ser adicionados facilmente
- **Composição Dinâmica**: Funcionalidades podem ser combinadas em tempo de execução

---

## Comparação dos Resultados

### Strategy Pattern (Replace Conditional with Polymorphism)
**Antes:**
- 1 classe com múltiplos if/else
- Difícil de estender
- Lógica misturada

**Depois:**
- Interface Strategy + implementações concretas
- Fácil adição de novas estratégias
- Cada estratégia testável independentemente

### Decorator Pattern (Move Embellishment to Decorator)
**Antes:**
- 1 classe com múltiplas responsabilidades
- Configuração através de flags booleanas
- Combinação limitada de funcionalidades

**Depois:**
- Interface Component + decorators
- Combinação flexível de funcionalidades
- Cada decorator com responsabilidade única

---

## 3. Comparação: Decorator Pattern vs Java Streams vs Builder Pattern

### Semelhanças Conceituais
Embora sejam conceitos diferentes, o **Decorator Pattern**, **Java Streams** e **Builder Pattern** compartilham características interessantes relacionadas à **composição** e **fluência**:

#### 🔗 **Composição de Funcionalidades**

**Decorator Pattern:**
```java
// Composição através de decorators aninhados
Relatorio relatorio = new CriptografiaDecorator(
    new CompressaoDecorator(
        new CabecalhoDecorator(
            new RelatorioBasico(dados)
        )
    )
);
```

**Java Streams:**
```java
// Composição através de operações intermediárias
List<String> resultado = dados.stream()
    .filter(item -> item.contains("Produto"))
    .map(item -> item.toUpperCase())
    .sorted()
    .collect(Collectors.toList());
```

**Builder Pattern:**
```java
// Composição através de métodos fluentes
RelatorioBuilder relatorio = new RelatorioBuilder()
    .comCabecalho()
    .comTimestamp()
    .comCompressao()
    .comCriptografia()
    .build();
```

#### 🎯 **Análise Comparativa**

| Aspecto | Decorator | Streams | Builder |
|---------|-----------|---------|---------|
| **Propósito** | Adicionar comportamentos dinamicamente | Processar coleções funcionalmente | Construir objetos complexos |
| **Composição** | Aninhamento de objetos | Pipeline de operações | Métodos fluentes |
| **Execução** | Imediata (ao chamar método) | Lazy (apenas no terminal) | Postergada (no build()) |
| **Reutilização** | Alta (decorators independentes) | Baixa (streams são consumidos) | Média (builder pode ser reutilizado) |
| **Flexibilidade** | Ordem pode ser alterada | Ordem das operações importa | Ordem flexível dos métodos |

#### 💡 **Implementação de um "Decorator Fluente"**

Podemos combinar os conceitos para criar uma versão fluente do Decorator:

```java
// Implementação híbrida - Decorator com interface fluente
public class RelatorioFluenteBuilder {
    private Relatorio relatorio;
    
    public RelatorioFluenteBuilder(List<String> dados) {
        this.relatorio = new RelatorioBasico(dados);
    }
    
    public RelatorioFluenteBuilder comCabecalho() {
        this.relatorio = new CabecalhoDecorator(this.relatorio);
        return this;
    }
    
    public RelatorioFluenteBuilder comRodape() {
        this.relatorio = new RodapeDecorator(this.relatorio);
        return this;
    }
    
    public RelatorioFluenteBuilder comTimestamp() {
        this.relatorio = new TimestampDecorator(this.relatorio);
        return this;
    }
    
    public RelatorioFluenteBuilder comAssinatura() {
        this.relatorio = new AssinaturaDecorator(this.relatorio);
        return this;
    }
    
    public RelatorioFluenteBuilder comCompressao() {
        this.relatorio = new CompressaoDecorator(this.relatorio);
        return this;
    }
    
    public RelatorioFluenteBuilder comCriptografia() {
        this.relatorio = new CriptografiaDecorator(this.relatorio);
        return this;
    }
    
    public String gerar() {
        return this.relatorio.gerar();
    }
}

// Uso com sintaxe fluente
String resultado = new RelatorioFluenteBuilder(dados)
    .comCabecalho()
    .comTimestamp()
    .comAssinatura()
    .comCompressao()
    .gerar();
```

#### 🔍 **Diferenças Fundamentais**

**1. Decorator Pattern:**
- **Objetivo:** Adicionar responsabilidades a objetos existentes
- **Estrutural:** Composição através de herança/interface
- **Runtime:** Comportamentos podem ser adicionados/removidos dinamicamente
- **Uso:** Quando você precisa de funcionalidades opcionais combinadas

**2. Java Streams:**
- **Objetivo:** Processamento funcional de coleções de dados
- **Funcional:** Pipeline de transformações sobre dados
- **Runtime:** Operações lazy avaliadas apenas no terminal
- **Uso:** Quando você precisa transformar/filtrar coleções

**3. Builder Pattern:**
- **Objetivo:** Construção de objetos complexos passo a passo
- **Criacional:** Separação da construção da representação
- **Runtime:** Configuração flexível antes da criação final
- **Uso:** Quando você tem objetos com muitos parâmetros opcionais

#### 🎨 **Quando Usar Cada Um**

**Use Decorator quando:**
- Precisar adicionar funcionalidades a objetos existentes
- As funcionalidades podem ser combinadas de várias formas
- Quiser evitar explosão de subclasses

**Use Streams quando:**
- Estiver processando coleções de dados
- Precisar de operações como filtro, mapeamento, redução
- Quiser código mais funcional e legível

**Use Builder quando:**
- O objeto tem muitos parâmetros de construção
- Alguns parâmetros são opcionais
- A ordem de configuração não importa

#### 🔗 **Combinação de Padrões**

É possível combinar esses padrões para soluções mais poderosas:

```java
// Exemplo: Processamento de relatórios com Streams + Decorator + Builder
List<RelatorioConfig> configs = Arrays.asList(
    new RelatorioConfig("vendas", true, false, true),
    new RelatorioConfig("financeiro", false, true, true)
);

List<String> relatoriosGerados = configs.stream()
    .map(config -> new RelatorioFluenteBuilder(config.getDados())
        .comCabecalho(config.temCabecalho())
        .comRodape(config.temRodape())
        .comAssinatura(config.temAssinatura())
        .gerar())
    .collect(Collectors.toList());
```

---

## Conclusão
As refatorações do livro "Refactoring to Patterns" demonstram como transformar código problemático em soluções elegantes usando padrões estabelecidos. Ambas as refatorações resultam em código mais limpo, extensível e testável, seguindo princípios SOLID e facilitando a manutenção a longo prazo.

A comparação entre Decorator, Streams e Builder revela que, embora tenham propósitos diferentes, todos compartilham o conceito de **composição fluente** e podem ser combinados para criar soluções ainda mais poderosas e expressivas.