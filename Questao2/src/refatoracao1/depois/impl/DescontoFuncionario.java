package refatoracao1.depois.impl;

import refatoracao1.depois.strategy.EstrategiaDesconto;

/**
 * Concrete Strategy - Implementação específica para funcionário Encapsula o comportamento de
 * desconto para funcionários
 */
public class DescontoFuncionario implements EstrategiaDesconto {

    @Override
    public double calcularDesconto(double valor) {
        return valor * 0.7; // 30% desconto
    }

    @Override
    public String obterMensagem() {
        return "Funcionário - desconto de 30%";
    }

    @Override
    public String obterCategoria() {
        return "Interna";
    }
}
