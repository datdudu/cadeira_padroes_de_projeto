package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Base Decorator - Classe abstrata que define a estrutura dos decorators
 * Resultado da refatoração "Move Embellishment to Decorator"
 */
public abstract class RelatorioDecorator implements Relatorio {
    protected Relatorio relatorio;
    
    public RelatorioDecorator(Relatorio relatorio) {
        this.relatorio = relatorio;
    }
    
    @Override
    public String gerar() {
        return relatorio.gerar();
    }
}
