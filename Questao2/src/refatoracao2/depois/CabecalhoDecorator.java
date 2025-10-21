package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona cabeçalho ao relatório Responsabilidade única: adicionar cabeçalho
 */
public class CabecalhoDecorator extends RelatorioDecorator {

    public CabecalhoDecorator(Relatorio relatorio) {
        super(relatorio);
    }

    @Override
    public String gerar() {
        return "=== RELATÓRIO DE VENDAS ===\n" + super.gerar();
    }
}
