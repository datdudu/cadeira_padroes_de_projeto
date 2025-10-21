import refatoracao1.depois.CalculadoraDesconto;
import refatoracao1.depois.factory.EstrategiaDescontoFactory;
import refatoracao1.depois.strategy.EstrategiaDesconto;

import refatoracao2.antes.RelatorioVendas;
import refatoracao2.depois.RelatorioBasico;
import refatoracao2.depois.CabecalhoDecorator;
import refatoracao2.depois.RodapeDecorator;
import refatoracao2.depois.TimestampDecorator;
import refatoracao2.depois.AssinaturaDecorator;
import refatoracao2.depois.CompressaoDecorator;
import refatoracao2.depois.CriptografiaDecorator;
import refatoracao2.depois.RelatorioFluenteBuilder;
import refatoracao2.depois.component.Relatorio;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstração das duas refatorações do livro "Refactoring to Patterns" 1. Replace Conditional with
 * Polymorphism → Strategy Pattern 2. Move Embellishment to Decorator → Decorator Pattern 3.
 * Comparação: Decorator vs Streams vs Builder Pattern
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== REFATORAÇÕES DO LIVRO REFACTORING TO PATTERNS ===\n");

        demonstrarRefatoracao1();
        System.out.println("\n============================================================\n");
        demonstrarRefatoracao2();
        System.out.println("\n============================================================\n");
        demonstrarComparacaoPadroes();
    }

    /**
     * Demonstra a Refatoração 1: Replace Conditional with Polymorphism → Strategy Pattern
     */
    private static void demonstrarRefatoracao1() {
        System.out
                .println("REFATORAÇÃO 1: Replace Conditional with Polymorphism → Strategy Pattern");
        System.out.println("Transforma código cheio de if/else em padrão Strategy\n");

        double valor = 1000.0;
        String[] tiposCliente = {"REGULAR", "VIP", "PREMIUM", "FUNCIONARIO"};

        System.out.println("--- ANTES DA REFATORAÇÃO (Código com if/else) ---");
        refatoracao1.antes.CalculadoraDesconto calculadoraAntes =
                new refatoracao1.antes.CalculadoraDesconto();

        for (String tipo : tiposCliente) {
            double valorComDesconto = calculadoraAntes.calcular(valor, tipo);
            String mensagem = calculadoraAntes.obterMensagem(tipo);
            String categoria = calculadoraAntes.obterCategoria(tipo);

            System.out.printf("Tipo: %s | Valor: R$ %.2f | Categoria: %s | %s%n", tipo,
                    valorComDesconto, categoria, mensagem);
        }

        System.out.println("\n--- DEPOIS DA REFATORAÇÃO (Strategy Pattern) ---");

        for (String tipo : tiposCliente) {
            // Utiliza factory para criar a estratégia apropriada
            EstrategiaDesconto estrategia = EstrategiaDescontoFactory.criar(tipo);
            CalculadoraDesconto calculadoraDepois = new CalculadoraDesconto(estrategia);

            double valorComDesconto = calculadoraDepois.calcular(valor);
            String mensagem = calculadoraDepois.obterMensagem();
            String categoria = calculadoraDepois.obterCategoria();

            System.out.printf("Tipo: %s | Valor: R$ %.2f | Categoria: %s | %s%n", tipo,
                    valorComDesconto, categoria, mensagem);
        }

        System.out.println("\nBenefícios da refatoração:");
        System.out.println("✓ Eliminação dos condicionais if/else");
        System.out.println("✓ Fácil adição de novos tipos de desconto");
        System.out.println("✓ Cada estratégia testável independentemente");
        System.out.println("✓ Princípio da Responsabilidade Única respeitado");
    }

    /**
     * Demonstra a Refatoração 2: Move Embellishment to Decorator → Decorator Pattern
     */
    private static void demonstrarRefatoracao2() {
        System.out.println("REFATORAÇÃO 2: Move Embellishment to Decorator → Decorator Pattern");
        System.out.println(
                "Transforma classe com múltiplas responsabilidades em Decorators flexíveis\n");

        List<String> dados = Arrays.asList("Produto A - Vendas: 150 unidades - R$ 15.000,00",
                "Produto B - Vendas: 200 unidades - R$ 30.000,00",
                "Produto C - Vendas: 100 unidades - R$ 10.000,00",
                "TOTAL: 450 unidades - R$ 55.000,00");

        System.out.println("--- ANTES DA REFATORAÇÃO (Classe com múltiplas responsabilidades) ---");
        RelatorioVendas relatorioAntes = new RelatorioVendas(dados);

        // Configuração através de flags booleanas
        relatorioAntes.setIncluirCabecalho(true);
        relatorioAntes.setIncluirTimestamp(true);
        relatorioAntes.setIncluirAssinatura(true);
        relatorioAntes.setIncluirRodape(true);
        relatorioAntes.setComprimirDados(true);

        System.out.println("Relatório com configurações fixas:");
        System.out.println(relatorioAntes.gerar());

        System.out.println("--- DEPOIS DA REFATORAÇÃO (Decorator Pattern) ---");

        // Exemplo 1: Relatório básico
        System.out.println("1. Relatório Básico:");
        Relatorio relatorioBasico = new RelatorioBasico(dados);
        System.out.println(relatorioBasico.gerar());

        // Exemplo 2: Relatório com cabeçalho e rodapé
        System.out.println("2. Relatório com Cabeçalho e Rodapé:");
        Relatorio relatorioComCabecalhoRodape =
                new RodapeDecorator(new CabecalhoDecorator(new RelatorioBasico(dados)));
        System.out.println(relatorioComCabecalhoRodape.gerar());

        // Exemplo 3: Relatório completo
        System.out.println("3. Relatório Completo (todos os decorators):");
        Relatorio relatorioCompleto = new CriptografiaDecorator(new CompressaoDecorator(
                new RodapeDecorator(new AssinaturaDecorator(new TimestampDecorator(
                        new CabecalhoDecorator(new RelatorioBasico(dados)))))));
        System.out.println(relatorioCompleto.gerar());

        // Exemplo 4: Combinação personalizada
        System.out.println("4. Combinação Personalizada (apenas timestamp e assinatura):");
        Relatorio relatorioPersonalizado =
                new AssinaturaDecorator(new TimestampDecorator(new RelatorioBasico(dados)));
        System.out.println(relatorioPersonalizado.gerar());

        System.out.println("Benefícios da refatoração:");
        System.out.println("✓ Responsabilidade única para cada decorator");
        System.out.println("✓ Combinações flexíveis de funcionalidades");
        System.out.println("✓ Fácil adição de novos decorators");
        System.out.println("✓ Composição dinâmica em tempo de execução");
    }

    /**
     * Demonstra a comparação entre Decorator Pattern, Java Streams e Builder Pattern
     */
    private static void demonstrarComparacaoPadroes() {
        System.out.println("COMPARAÇÃO: Decorator Pattern vs Java Streams vs Builder Pattern");
        System.out.println(
                "Demonstrando semelhanças e diferenças na composição de funcionalidades\n");

        List<String> dados = Arrays.asList("Produto A - Vendas: 150 unidades - R$ 15.000,00",
                "Produto B - Vendas: 200 unidades - R$ 30.000,00",
                "Produto C - Vendas: 100 unidades - R$ 10.000,00",
                "TOTAL: 450 unidades - R$ 55.000,00");

        System.out
                .println("=== 1. DECORATOR PATTERN (Composição através de objetos aninhados) ===");
        Relatorio decoratorTradicional = new CriptografiaDecorator(new CompressaoDecorator(
                new RodapeDecorator(new CabecalhoDecorator(new RelatorioBasico(dados)))));
        System.out.println("Resultado do Decorator tradicional:");
        System.out.println(decoratorTradicional.gerar());

        System.out.println("=== 2. BUILDER PATTERN + DECORATOR (Interface fluente) ===");
        String resultadoBuilder = RelatorioFluenteBuilder.criar(dados).comCabecalho().comRodape()
                .comCompressao().comCriptografia().gerar();
        System.out.println("Resultado do Builder fluente:");
        System.out.println(resultadoBuilder);

        System.out.println("=== 3. JAVA STREAMS (Processamento funcional de dados) ===");
        // Simulando processamento de dados com streams (conceito similar de composição)
        List<String> dadosProcessados = dados.stream().filter(linha -> linha.contains("Produto")) // Filtro
                .map(linha -> "PROCESSADO: " + linha.toUpperCase()) // Transformação
                .map(linha -> linha + " [VERIFICADO]") // Outra transformação
                .collect(Collectors.toList());

        System.out.println("Dados processados com Streams:");
        dadosProcessados.forEach(System.out::println);

        System.out.println("\n=== 4. COMPARAÇÃO PRÁTICA ===");

        // Medindo flexibilidade: Decorator tradicional
        System.out.println("\n4.1. Flexibilidade - Decorator Tradicional:");
        Relatorio relatorio1 = new CabecalhoDecorator(new RelatorioBasico(dados));
        Relatorio relatorio2 =
                new RodapeDecorator(new CabecalhoDecorator(new RelatorioBasico(dados)));
        System.out.println("Só cabeçalho: " + relatorio1.gerar().split("\n").length + " linhas");
        System.out.println(
                "Cabeçalho + rodapé: " + relatorio2.gerar().split("\n").length + " linhas");

        // Medindo flexibilidade: Builder fluente
        System.out.println("\n4.2. Flexibilidade - Builder Fluente:");
        String resultado1 = RelatorioFluenteBuilder.criar(dados).comCabecalho().gerar();
        String resultado2 = RelatorioFluenteBuilder.criar(dados).comCabecalho().comRodape().gerar();
        System.out.println("Só cabeçalho: " + resultado1.split("\n").length + " linhas");
        System.out.println("Cabeçalho + rodapé: " + resultado2.split("\n").length + " linhas");

        // Medindo flexibilidade: Streams
        System.out.println("\n4.3. Flexibilidade - Streams:");
        long totalProdutos = dados.stream().filter(linha -> linha.contains("Produto")).count();
        double totalVendas = dados.stream().filter(linha -> linha.contains("TOTAL"))
                .map(linha -> linha.replaceAll("[^0-9.]", "")).filter(valor -> !valor.isEmpty())
                .mapToDouble(Double::parseDouble).sum();
        System.out.println("Total de produtos: " + totalProdutos);
        System.out.println("Valor total extraído: R$ " + totalVendas);

        System.out.println("\n=== 5. ANÁLISE COMPARATIVA ===");
        System.out.println(
                "┌─────────────────┬─────────────────┬─────────────────┬─────────────────┐");
        System.out.println(
                "│     Aspecto     │    Decorator    │     Streams     │     Builder     │");
        System.out.println(
                "├─────────────────┼─────────────────┼─────────────────┼─────────────────┤");
        System.out.println(
                "│    Propósito    │ Adicionar comp. │ Processar dados │ Construir obj.  │");
        System.out.println(
                "│   Composição    │ Objetos aninhos │ Pipeline ops    │ Métodos fluentes│");
        System.out.println(
                "│    Execução     │    Imediata     │ Lazy evaluation │   No build()    │");
        System.out.println(
                "│  Reutilização   │      Alta       │     Baixa       │     Média       │");
        System.out.println(
                "│ Flexibilidade   │ Ordem alterável │ Ordem importa   │ Ordem flexível  │");
        System.out.println(
                "│   Legibilidade  │     Média       │      Alta       │      Alta       │");
        System.out.println(
                "└─────────────────┴─────────────────┴─────────────────┴─────────────────┘");

        System.out.println("\n=== 6. QUANDO USAR CADA PADRÃO ===");
        System.out.println(
                "🎯 DECORATOR: Quando precisar adicionar funcionalidades a objetos existentes");
        System.out.println("   • Funcionalidades podem ser combinadas de várias formas");
        System.out.println("   • Evitar explosão de subclasses");
        System.out.println("   • Exemplo: Sistemas de notificação, processamento de dados");

        System.out.println("\n🌊 STREAMS: Quando estiver processando coleções de dados");
        System.out.println("   • Operações como filtro, mapeamento, redução");
        System.out.println("   • Código mais funcional e legível");
        System.out.println("   • Exemplo: Análise de dados, transformações em lote");

        System.out.println("\n🏗️ BUILDER: Quando o objeto tem muitos parâmetros de construção");
        System.out.println("   • Alguns parâmetros são opcionais");
        System.out.println("   • Ordem de configuração não importa");
        System.out.println("   • Exemplo: Configurações complexas, objetos de domínio");

        System.out.println(
                "\n💡 COMBINAÇÃO: Todos podem ser combinados para soluções mais poderosas!");
        System.out.println("   • Builder + Decorator = Interface fluente para decoração");
        System.out.println("   • Streams + Builder = Processamento e construção em pipeline");
        System.out.println("   • Decorator + Streams = Decoração baseada em critérios de dados");
    }
}
