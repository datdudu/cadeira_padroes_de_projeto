package refatoracao1.depois;

import refatoracao1.depois.strategy.EstrategiaDesconto;

/**
 * Context - Classe que utiliza as estratégias de desconto Resultado da refatoração "Replace
 * Conditional with Polymorphism" Elimina os condicionais if/else através do uso do padrão Strategy
 */
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

    public String obterCategoria() {
        return estrategia.obterCategoria();
    }
}
