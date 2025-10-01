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
import refatoracao2.depois.component.Relatorio;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstração das duas refatorações do livro "Refactoring to Patterns"
 * 1. Replace Conditional with Polymorphism → Strategy Pattern
 * 2. Move Embellishment to Decorator → Decorator Pattern
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== REFATORAÇÕES DO LIVRO REFACTORING TO PATTERNS ===\n");
        
        demonstrarRefatoracao1();
        System.out.println("\n============================================================\n");
        demonstrarRefatoracao2();
    }
    
    /**
     * Demonstra a Refatoração 1: Replace Conditional with Polymorphism → Strategy Pattern
     */
    private static void demonstrarRefatoracao1() {
        System.out.println("REFATORAÇÃO 1: Replace Conditional with Polymorphism → Strategy Pattern");
        System.out.println("Transforma código cheio de if/else em padrão Strategy\n");
        
        double valor = 1000.0;
        String[] tiposCliente = {"REGULAR", "VIP", "PREMIUM", "FUNCIONARIO"};
        
        System.out.println("--- ANTES DA REFATORAÇÃO (Código com if/else) ---");
        refatoracao1.antes.CalculadoraDesconto calculadoraAntes = new refatoracao1.antes.CalculadoraDesconto();
        
        for (String tipo : tiposCliente) {
            double valorComDesconto = calculadoraAntes.calcular(valor, tipo);
            String mensagem = calculadoraAntes.obterMensagem(tipo);
            String categoria = calculadoraAntes.obterCategoria(tipo);
            
            System.out.printf("Tipo: %s | Valor: R$ %.2f | Categoria: %s | %s%n", 
                            tipo, valorComDesconto, categoria, mensagem);
        }
        
        System.out.println("\n--- DEPOIS DA REFATORAÇÃO (Strategy Pattern) ---");
        
        for (String tipo : tiposCliente) {
            // Utiliza factory para criar a estratégia apropriada
            EstrategiaDesconto estrategia = EstrategiaDescontoFactory.criar(tipo);
            CalculadoraDesconto calculadoraDepois = new CalculadoraDesconto(estrategia);
            
            double valorComDesconto = calculadoraDepois.calcular(valor);
            String mensagem = calculadoraDepois.obterMensagem();
            String categoria = calculadoraDepois.obterCategoria();
            
            System.out.printf("Tipo: %s | Valor: R$ %.2f | Categoria: %s | %s%n", 
                            tipo, valorComDesconto, categoria, mensagem);
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
        System.out.println("Transforma classe com múltiplas responsabilidades em Decorators flexíveis\n");
        
        List<String> dados = Arrays.asList(
            "Produto A - Vendas: 150 unidades - R$ 15.000,00",
            "Produto B - Vendas: 200 unidades - R$ 30.000,00",
            "Produto C - Vendas: 100 unidades - R$ 10.000,00",
            "TOTAL: 450 unidades - R$ 55.000,00"
        );
        
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
        Relatorio relatorioComCabecalhoRodape = new RodapeDecorator(
            new CabecalhoDecorator(
                new RelatorioBasico(dados)
            )
        );
        System.out.println(relatorioComCabecalhoRodape.gerar());
        
        // Exemplo 3: Relatório completo
        System.out.println("3. Relatório Completo (todos os decorators):");
        Relatorio relatorioCompleto = new CriptografiaDecorator(
            new CompressaoDecorator(
                new RodapeDecorator(
                    new AssinaturaDecorator(
                        new TimestampDecorator(
                            new CabecalhoDecorator(
                                new RelatorioBasico(dados)
                            )
                        )
                    )
                )
            )
        );
        System.out.println(relatorioCompleto.gerar());
        
        // Exemplo 4: Combinação personalizada
        System.out.println("4. Combinação Personalizada (apenas timestamp e assinatura):");
        Relatorio relatorioPersonalizado = new AssinaturaDecorator(
            new TimestampDecorator(
                new RelatorioBasico(dados)
            )
        );
        System.out.println(relatorioPersonalizado.gerar());
        
        System.out.println("Benefícios da refatoração:");
        System.out.println("✓ Responsabilidade única para cada decorator");
        System.out.println("✓ Combinações flexíveis de funcionalidades");
        System.out.println("✓ Fácil adição de novos decorators");
        System.out.println("✓ Composição dinâmica em tempo de execução");
    }
}