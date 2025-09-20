package infra.service.Repository;

import domain.DataMapper.Livro;
import domain.repository.LivroRepository;

import java.util.List;

public class BibliotecaService {
    private final LivroRepository repository;

    public BibliotecaService(LivroRepository repository) {
        this.repository = repository;
    }

    public void adicionarLivro(String titulo, String autor, String isbn) {
        Livro livro = new Livro(null, titulo, autor, isbn, true);
        repository.salvar(livro);
    }

    public void emprestarLivro(Long id) {
        repository.buscarPorId(id).ifPresent(livro -> {
            livro.emprestar();
            repository.salvar(livro); // re-sincroniza o agregado
        });
    }

    public void devolverLivro(Long id) {
        repository.buscarPorId(id).ifPresent(livro -> {
            livro.devolver();
            repository.salvar(livro);
        });
    }

    public List<Livro> listarLivrosDisponiveis() {
        return repository.buscarDisponiveis();
    }
}