package domain.repository;
import domain.DataMapper.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroRepository {
    void salvar(Livro livro);
    void remover(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarTodos();
    List<Livro> buscarDisponiveis();
    List<Livro> buscarPorAutor(String autor);
}
