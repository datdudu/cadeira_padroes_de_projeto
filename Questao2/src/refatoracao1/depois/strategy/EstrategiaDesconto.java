package refatoracao1.depois.strategy;

/**
 * Strategy Interface - Define o contrato para todas as estratégias de desconto Aplicação da
 * refatoração "Replace Conditional with Polymorphism"
 */
public interface EstrategiaDesconto {
    double calcularDesconto(double valor);

    String obterMensagem();

    String obterCategoria();
}
