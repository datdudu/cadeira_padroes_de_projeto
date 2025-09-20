package infra.impl;

import domain.DataMapper.Livro;
import infra.LivroMapper;

import java.sql.*;
import java.util.*;

public class JdbcLivroMapper implements LivroMapper {
    private Connection connection;

    public JdbcLivroMapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, isbn, disponivel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // não alteramos o livro diretamente (imutabilidade do domínio)
                System.out.println("Livro persistido com id: " + rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro", e);
        }
    }

    @Override
    public void update(Livro livro) {
        String sql = "UPDATE livros SET titulo=?, autor=?, isbn=?, disponivel=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.setLong(5, livro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM livros WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar livro", e);
        }
    }

    @Override
    public Optional<Livro> findById(Long id) {
        String sql = "SELECT * FROM livros WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro", e);
        }
    }

    @Override
    public List<Livro> findAll() {
        String sql = "SELECT * FROM livros";
        List<Livro> livros = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros", e);
        }
        return livros;
    }

    private Livro mapRow(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getBoolean("disponivel")
        );
    }
}