package refatoracao1.depois.impl;

import refatoracao1.depois.strategy.EstrategiaDesconto;

/**
 * Concrete Strategy - Implementação específica para cliente VIP
 * Encapsula o comportamento de desconto para clientes VIP
 */
public class DescontoVIP implements EstrategiaDesconto {
    
    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.9; // 10% desconto
    }
    
    @Override
    public String obterMensagem() {
        return "Cliente VIP - desconto de 10%";
    }
    
    @Override
    public String obterCategoria() {
        return "Especial";
    }
}
