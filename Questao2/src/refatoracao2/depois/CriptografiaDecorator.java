package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona criptografia ao relatório Responsabilidade única: criptografar
 * dados
 */
public class CriptografiaDecorator extends RelatorioDecorator {

    public CriptografiaDecorator(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        String dados = super.gerar();
        return criptografar(dados);
    }

    private String criptografar(String dados) {
        // Simulação de criptografia
        return "[CRIPTOGRAFADO]" + dados + "[/CRIPTOGRAFADO]";
    }
}
