package refatoracao2.antes;

import java.util.List;

/**
 * Código ANTES da refatoração - classe cheia de responsabilidades extras
 * Problema: Múltiplas funcionalidades opcionais em uma única classe,
 * tornando-a complexa e difícil de manter
 */
public class RelatorioVendas {
    private List<String> dados;
    private boolean incluirCabecalho;
    private boolean incluirRodape;
    private boolean incluirTimestamp;
    private boolean incluirAssinatura;
    private boolean comprimirDados;
    private boolean criptografarDados;
    
    public RelatorioVendas(List<String> dados) {
        this.dados = dados;
        this.incluirCabecalho = false;
        this.incluirRodape = false;
        this.incluirTimestamp = false;
        this.incluirAssinatura = false;
        this.comprimirDados = false;
        this.criptografarDados = false;
    }
    
    // Métodos para configurar opções (múltiplas responsabilidades)
    public void setIncluirCabecalho(boolean incluir) { 
        this.incluirCabecalho = incluir; 
    }
    
    public void setIncluirRodape(boolean incluir) { 
        this.incluirRodape = incluir; 
    }
    
    public void setIncluirTimestamp(boolean incluir) { 
        this.incluirTimestamp = incluir; 
    }
    
    public void setIncluirAssinatura(boolean incluir) { 
        this.incluirAssinatura = incluir; 
    }
    
    public void setComprimirDados(boolean comprimir) { 
        this.comprimirDados = comprimir; 
    }
    
    public void setCriptografarDados(boolean criptografar) { 
        this.criptografarDados = criptografar; 
    }
    
    public String gerar() {
        StringBuilder relatorio = new StringBuilder();
        
        // Lógica complexa com múltiplas responsabilidades
        if (incluirCabecalho) {
            relatorio.append("=== RELATÓRIO DE VENDAS ===\n");
        }
        
        if (incluirTimestamp) {
            relatorio.append("Data/Hora: ").append(java.time.LocalDateTime.now()).append("\n");
        }
        
        // Dados principais
        for (String linha : dados) {
            relatorio.append(linha).append("\n");
        }
        
        if (incluirAssinatura) {
            relatorio.append("\nAssinado digitalmente por: Sistema de Vendas v1.0\n");
        }
        
        if (incluirRodape) {
            relatorio.append("=== FIM DO RELATÓRIO ===\n");
        }
        
        String resultado = relatorio.toString();
        
        // Processamentos adicionais
        if (comprimirDados) {
            resultado = comprimir(resultado);
        }
        
        if (criptografarDados) {
            resultado = criptografar(resultado);
        }
        
        return resultado;
    }
    
    private String comprimir(String dados) {
        // Simulação de compressão
        return "[COMPRIMIDO]" + dados + "[/COMPRIMIDO]";
    }
    
    private String criptografar(String dados) {
        // Simulação de criptografia
        return "[CRIPTOGRAFADO]" + dados + "[/CRIPTOGRAFADO]";
    }
}
