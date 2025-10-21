package refatoracao1.depois.factory;

import refatoracao1.depois.strategy.EstrategiaDesconto;
import refatoracao1.depois.impl.DescontoRegular;
import refatoracao1.depois.impl.DescontoVIP;
import refatoracao1.depois.impl.DescontoPremium;
import refatoracao1.depois.impl.DescontoFuncionario;

/**
 * Factory para criar as estratégias de desconto Centraliza a criação das estratégias e mantém a
 * flexibilidade
 */
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
