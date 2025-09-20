package infra;

// Interface DAO
import domain.DAO.Livro;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LivroDAO {
    void salvar(Livro livro) throws SQLException;
    void atualizar(Livro livro);
    void deletar(Long id);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarTodos() throws SQLException;
    List<Livro> buscarPorAutor(String autor);
    List<Livro> buscarDisponiveis();
}