package infra.impl;

import domain.DataMapper.Livro;
import domain.repository.LivroRepository;

import java.sql.*;
import java.util.*;

public class JdbcLivroRepository implements LivroRepository {
    private final Connection connection;

    public JdbcLivroRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            inserir(livro);
        } else {
            atualizar(livro);
        }
    }

    private void inserir(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, isbn, disponivel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getIsbn());
            stmt.setBoolean(4, livro.isDisponivel());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Livro inserido com id " + rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro", e);
        }
    }

    private void atualizar(Livro livro) {
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
    public void remover(Livro livro) {
        String sql = "DELETE FROM livros WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, livro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover livro", e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String sql = "SELECT * FROM livros WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro", e);
        }
    }

    @Override
    public List<Livro> buscarTodos() {
        String sql = "SELECT * FROM livros";
        return executarConsulta(sql);
    }

    @Override
    public List<Livro> buscarDisponiveis() {
        String sql = "SELECT * FROM livros WHERE disponivel=true";
        return executarConsulta(sql);
    }

    @Override
    public List<Livro> buscarPorAutor(String autor) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE autor LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + autor + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) livros.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros por autor", e);
        }
        return livros;
    }

    private List<Livro> executarConsulta(String sql) {
        List<Livro> livros = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) livros.add(mapRow(rs));
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