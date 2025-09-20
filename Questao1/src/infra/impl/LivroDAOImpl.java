package infra.impl;

import domain.DAO.Livro;
import infra.LivroDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroDAOImpl implements LivroDAO {
    private Connection connection;

    @Override
    public void salvar(Livro livro) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, isbn, disponivel) VALUES (?, ?, ?, ?)";
        // SQL + mapeamento tudo junto
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.executeUpdate();
        }
    }

    @Override
    public void atualizar(Livro livro) {

    }

    @Override
    public void deletar(Long id) {

    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Livro> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM livros";
        List<Livro> livros = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Mapeamento inline (misturado com SQL)
                Livro livro = new Livro();
                livro.setId(rs.getLong("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setDisponivel(rs.getBoolean("disponivel"));
                livros.add(livro);
            }
        }
        return livros;
    }

    @Override
    public List<Livro> buscarPorAutor(String autor) {
        return null;
    }

    @Override
    public List<Livro> buscarDisponiveis() {
        return null;
    }
}