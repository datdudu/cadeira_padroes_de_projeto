package infra.service.Mapper;

import domain.DataMapper.Livro;
import infra.LivroMapper;

import java.util.List;

public class BibliotecaService {
    private final LivroMapper mapper;

    public BibliotecaService(LivroMapper mapper) {
        this.mapper = mapper;
    }

    public void cadastrarLivro(String titulo, String autor, String isbn) {
        Livro livro = new Livro(null, titulo, autor, isbn, true);
        mapper.insert(livro);
    }

    public void emprestarLivro(Long id) {
        mapper.findById(id).ifPresent(livro -> {
            livro.emprestar(); // regra de negócio
            mapper.update(livro); // sincroniza no banco
        });
    }

    public void devolverLivro(Long id) {
        mapper.findById(id).ifPresent(livro -> {
            livro.devolver();
            mapper.update(livro);
        });
    }

    public List<Livro> listarLivros() {
        return mapper.findAll();
    }
}