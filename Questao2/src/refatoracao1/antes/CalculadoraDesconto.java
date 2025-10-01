package refatoracao1.antes;

/**
 * Código ANTES da refatoração - cheio de if/else
 * Problema: Múltiplos condicionais que executam comportamentos diferentes
 * baseados em tipos de cliente. Difícil de manter e estender.
 */
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
            throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
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
            throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
        }
    }
    
    public String obterCategoria(String tipoCliente) {
        if (tipoCliente.equals("REGULAR")) {
            return "Básica";
        } else if (tipoCliente.equals("VIP")) {
            return "Especial";
        } else if (tipoCliente.equals("PREMIUM")) {
            return "Premium";
        } else if (tipoCliente.equals("FUNCIONARIO")) {
            return "Interna";
        } else {
            throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
        }
    }
}
