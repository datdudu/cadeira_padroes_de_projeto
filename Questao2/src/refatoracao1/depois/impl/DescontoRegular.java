package refatoracao1.depois.impl;

import refatoracao1.depois.strategy.EstrategiaDesconto;

/**
 * Concrete Strategy - Implementação específica para cliente regular
 * Encapsula o comportamento de desconto para clientes regulares
 */
public class DescontoRegular implements EstrategiaDesconto {
    
    @Override
    public double calcularDesconto(double valor) {
        return valor; // Sem desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Cliente regular - sem desconto especial";
    }
    
    @Override
    public String obterCategoria() {
        return "Básica";
    }
}
