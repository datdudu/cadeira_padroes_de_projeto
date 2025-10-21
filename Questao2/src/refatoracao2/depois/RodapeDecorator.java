package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona rodapé ao relatório Responsabilidade única: adicionar rodapé
 */
public class RodapeDecorator extends RelatorioDecorator {

    public RodapeDecorator(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        return super.gerar() + "=== FIM DO RELATÓRIO ===\n";
    }
}
