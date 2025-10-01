package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona assinatura ao relatório
 * Responsabilidade única: adicionar assinatura digital
 */
public class AssinaturaDecorator extends RelatorioDecorator {
    
    public AssinaturaDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        return super.gerar() + "\nAssinado digitalmente por: Sistema de Vendas v1.0\n";
    }
}
