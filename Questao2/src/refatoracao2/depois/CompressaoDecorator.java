package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona compressão ao relatório
 * Responsabilidade única: comprimir dados
 */
public class CompressaoDecorator extends RelatorioDecorator {
    
    public CompressaoDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        String dados = super.gerar();
        return comprimir(dados);
    }
    
    private String comprimir(String dados) {
        // Simulação de compressão
        return "[COMPRIMIDO]" + dados + "[/COMPRIMIDO]";
    }
}
