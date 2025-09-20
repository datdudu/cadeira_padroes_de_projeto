package infra.service.DAO;

import domain.DAO.Livro;
import infra.LivroDAO;

import java.util.List;
import java.util.Optional;

// Serviço que utiliza o DAO
public class BibliotecaService {
    private LivroDAO livroDAO;

    public BibliotecaService(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public void cadastrarLivro(String titulo, String autor, String isbn) {
        Livro livro = new Livro(titulo, autor, isbn);
        livroDAO.salvar(livro);
    }

    public boolean emprestarLivro(Long id) {
        Optional<Livro> livroOpt = livroDAO.buscarPorId(id);

        if (livroOpt.isPresent() && livroOpt.get().isDisponivel()) {
            Livro livro = livroOpt.get();
            livro.setDisponivel(false);
            livroDAO.atualizar(livro);
            return true;
        }
        return false;
    }

    public void devolverLivro(Long id) {
        Optional<Livro> livroOpt = livroDAO.buscarPorId(id);

        if (livroOpt.isPresent()) {
            Livro livro = livroOpt.get();
            livro.setDisponivel(true);
            livroDAO.atualizar(livro);
        }
    }

    public List<Livro> consultarLivrosDisponiveis() {
        return livroDAO.buscarDisponiveis();
    }
}