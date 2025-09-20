package domain.DataMapper;

// Entidade de domínio pura
public class Livro {
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private boolean disponivel = true;

    public Livro(Long id, String titulo, String autor, String isbn, boolean disponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.disponivel = disponivel;
    }

    // Métodos de negócio
    public void emprestar() {
        if (!disponivel) throw new IllegalStateException("Livro já emprestado");
        this.disponivel = false;
    }

    public void devolver() {
        this.disponivel = true;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public boolean isDisponivel() { return disponivel; }
}