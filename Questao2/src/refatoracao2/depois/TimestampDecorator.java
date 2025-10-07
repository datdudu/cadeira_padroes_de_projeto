package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;

/**
 * Concrete Decorator - Adiciona timestamp ao relatório
 * Responsabilidade única: adicionar timestamp
 */
public class TimestampDecorator extends RelatorioDecorator {
    
    public TimestampDecorator(Relatorio relatorio) {
        super(relatorio);
    }
    
    @Override
    public String gerar() {
        String timestamp = "Data/Hora: " + java.time.LocalDateTime.now() + "\n";
        return timestamp + super.gerar();
    }
}
