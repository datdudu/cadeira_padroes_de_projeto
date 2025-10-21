package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;
import java.util.List;

/**
 * Concrete Component - Implementação básica do relatório Responsabilidade única: gerar o relatório
 * básico com os dados
 */
public class RelatorioBasico implements Relatorio {
    private List<String> dados;

    public RelatorioBasico(List<String> dados) {
        this.dados = dados;
    }

    @Override
    public String gerar() {
        StringBuilder relatorio = new StringBuilder();
        for (String linha : dados) {
            relatorio.append(linha).append("\n");
        }
        return relatorio.toString();
    }
}
