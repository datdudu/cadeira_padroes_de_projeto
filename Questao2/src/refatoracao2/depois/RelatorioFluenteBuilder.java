package refatoracao2.depois;

import refatoracao2.depois.component.Relatorio;
import java.util.List;

/**
 * Implementação híbrida: Decorator Pattern + Builder Pattern Demonstra como combinar padrões para
 * criar uma interface fluente que utiliza decorators internamente
 */
public class RelatorioFluenteBuilder {
  private Relatorio relatorio;

  public RelatorioFluenteBuilder(List<String> dados) {
    this.relatorio = new RelatorioBasico(dados);
  }

  /**
   * Adiciona cabeçalho ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comCabecalho(boolean incluir) {
    if (incluir) {
      this.relatorio = new CabecalhoDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona cabeçalho ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comCabecalho() {
    return comCabecalho(true);
  }

  /**
   * Adiciona rodapé ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comRodape(boolean incluir) {
    if (incluir) {
      this.relatorio = new RodapeDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona rodapé ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comRodape() {
    return comRodape(true);
  }

  /**
   * Adiciona timestamp ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comTimestamp(boolean incluir) {
    if (incluir) {
      this.relatorio = new TimestampDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona timestamp ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comTimestamp() {
    return comTimestamp(true);
  }

  /**
   * Adiciona assinatura ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comAssinatura(boolean incluir) {
    if (incluir) {
      this.relatorio = new AssinaturaDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona assinatura ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comAssinatura() {
    return comAssinatura(true);
  }

  /**
   * Adiciona compressão ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comCompressao(boolean incluir) {
    if (incluir) {
      this.relatorio = new CompressaoDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona compressão ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comCompressao() {
    return comCompressao(true);
  }

  /**
   * Adiciona criptografia ao relatório se a condição for verdadeira
   */
  public RelatorioFluenteBuilder comCriptografia(boolean incluir) {
    if (incluir) {
      this.relatorio = new CriptografiaDecorator(this.relatorio);
    }
    return this;
  }

  /**
   * Adiciona criptografia ao relatório (sempre)
   */
  public RelatorioFluenteBuilder comCriptografia() {
    return comCriptografia(true);
  }

  /**
   * Constrói e retorna o relatório final
   */
  public Relatorio build() {
    return this.relatorio;
  }

  /**
   * Constrói e gera o relatório final em uma única operação
   */
  public String gerar() {
    return this.relatorio.gerar();
  }

  /**
   * Método estático para criar um builder de forma mais fluente
   */
  public static RelatorioFluenteBuilder criar(List<String> dados) {
    return new RelatorioFluenteBuilder(dados);
  }
}
