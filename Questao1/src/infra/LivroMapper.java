package infra;

import domain.DataMapper.Livro;

import java.util.List;
import java.util.Optional;

public interface LivroMapper {
    void insert(Livro livro);
    void update(Livro livro);
    void delete(Long id);
    Optional<Livro> findById(Long id);
    List<Livro> findAll();
}
