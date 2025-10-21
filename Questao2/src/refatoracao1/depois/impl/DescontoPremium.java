package refatoracao1.depois.impl;

import refatoracao1.depois.strategy.EstrategiaDesconto;

/**
 * Concrete Strategy - Implementação específica para cliente Premium Encapsula o comportamento de
 * desconto para clientes Premium
 */
public class DescontoPremium implements EstrategiaDesconto {

    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.8; // 20% desconto
    }

    @Override
    public String obterMensagem() {
        return "Cliente Premium - desconto de 20%";
    }

    @Override
    public String obterCategoria() {
        return "Premium";
    }
}
