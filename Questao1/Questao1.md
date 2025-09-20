# 1. Em relação aos padrões de persistencia: DAO,Data Mapper e Repository

## a) Compare-os no nível conceitual
### DAO (Data Access Object)
- O data access object ele foca em abstrair toda a parte de acesso a fonte de dados (persistencia), fazendo com que a implementação do acesso a fonte de dados possa ser alterado sem modificar o código do cliente
- O domínio não é separado da lógica de acesso a dados
### DataMapper
- Tem o propósito de separar totalmente a lógica de domínio e a persistência de dados
- Geralmente faz o intermédio (mapeamento do objeto de domínio, para o objeto de persistência)
- Pode ser usado junto ao DAO, onde atuaria na hora da persistencia de dados, fazendo o mapeamento do objeto de domínio para o objeto de persistencia
### Repository
- Esconde completamente a lógica de persistencia de dados
- Atua como uma API de alto nível para consulta e manipulação de agregados (objetos do domínio)
## b) Ilustre a diferença entre os mesmos em código

### Data Access Object (DAO)
- Abstrai o acesso a fonte de dados:
```Java
public interface LivroDAO {
    void salvar(Livro livro);
    void atualizar(Livro livro);
    void deletar(Long id);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarTodos();
    List<Livro> buscarPorAutor(String autor);
    List<Livro> buscarDisponiveis();
}
```
- Mas acessa o objeto diretamente na sua implementação (Objeto de Dominio também é o objeto de persistencia nessa lógica)
- Também modifica a entidade utilizando operadores set, trazendo o domínio para mais perto da lógica de persistencia, apesar de abstraí-la
```java
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
```

### Data Mapper
- Separa totalmente a camada a lógica de domínio para a de persistencia
- Utiliza o mapRow como mapeamento, domínio não tem propriedades de set, apenas puxando informações, utilizando mapper como forma de transitar dados
```java
public interface LivroMapper {
    void insert(Livro livro);
    void update(Livro livro);
    void delete(Long id);
    Optional<Livro> findById(Long id);
    List<Livro> findAll();
}
```
```java
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
```
```java
//....
private Livro mapRow(ResultSet rs) throws SQLException {
    return new Livro(
            rs.getLong("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("isbn"),
            rs.getBoolean("disponivel")
    );
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
```

### Repository
- Atua como uma API de alto nível para o domínio
- _"O que esse objeto domínio pode fazer"_
```java
public interface LivroRepository {
    void salvar(Livro livro);
    void remover(Livro livro);
    Optional<Livro> buscarPorId(Long id);
    List<Livro> buscarTodos();
    List<Livro> buscarDisponiveis();
    List<Livro> buscarPorAutor(String autor);
}
```
- Sua implementação não implementa lógica de persistencia, mas utiliza de métodos de persistencia para cumprir seu contrato com o domínio
```java
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
```
- Vemos aqui a implementação de salvar, utilizando um método de inserir, que esse sim faz acesso ao banco
- Pode ser implementado com DataMapper, DAO, seguindo a seguinte estrutura de responsabilidade:
```UML
[Camada de Domínio]          [Camada de Persistência]

   +---------------------+
   |      Livro          |   ← Entidade pura de negócio
   +---------------------+

   +---------------------+      
   |   LivroRepository   |   ← Agregado visto como coleção
   |  + buscarTodos()    |
   |  + salvar(livro)    |
   +---------------------+
              |
   ------------------------------
              v
   +---------------------+
   |     LivroDAO        |   ← API CRUD da tabela
   |  +insert(Livro)     |
   |  +update(Livro)     |
   |  +findById(id)      |
   +---------------------+
              |
   ------------------------------
              v
   +---------------------+
   |    LivroMapper      |   ← Tradutor objeto ↔ DB
   | mapRow(ResultSet)   |
   | toValues(Livro)     |
   +---------------------+
```
- Repository representa o que o objeto de dominio pode fazer
- Seus método do repository vao utilizar um DAO para que possam realizar persistencia (Repository -> DAO -> Banco de Dados)
- Uso do Mapper pode ajudar a trafegar esses dados, trazendo de objeto de dominio para objeto de persistencia e vice versa (Dominio -> Repository -> DataMapper -> DAO -> Banco de Dados // Banco de Dados -> DAO -> DataMapper -> Repository -> Dominio) 
## c) Explique a afirmação abaixo ilustrando com código o que você entendeu por agregado e invariantes:
```
No contexto de Domain-Driven Disegin **Repository** trabalha especificamente com agregados. Esta é uma das características fundamentais que distingue o padrão Repository de outras abstrações de acessos a dados. Segundo Eric Evans:

- Cada Repostiory gerencia apenas um tipo de agregado (não entidades individuais dentro do agregado)
- O Repository recupera e persiste o agregado como uma unidade completa
- Mantem a consistencia e as invariantes do agregado
```

- Agregado seria o objeto trabalhado (Objeto de domínio com sua lógica de negócio) e todas suas dependencias
- Invariante seria o que não pode ser alterado independente de que forma o objeto é feito/contruído, se referindo a suas lógicas de negócio

- Vemos abaixo o repository e sua implementação, tratando o objeto de domínio como agregado só
  - Um ItemPedido só existe abaixo de um Pedido
  - Quando voce traz o Pedido, voce traz todos os itens pedidos
  - Os dois são um agregado

```java
// Repository gerencia APENAS o agregado Pedido (não ItemPedido individualmente)
public interface PedidoRepository {
    void salvar(Pedido pedido);           // Salva o agregado completo
    Optional<Pedido> buscarPorId(Long id); // Recupera o agregado completo
    List<Pedido> buscarPorCliente(String clienteId); //Iremos usar ele como exemplo
    void remover(Pedido pedido);          // Remove o agregado completo
}

```
```java
@Override
    public Optional<Pedido> buscarPorId(Long id) {
        // Recupera o agregado COMPLETO (pedido + todos os itens)
        String sqlPedido = "SELECT * FROM pedidos WHERE id = ?";
        String sqlItens = "SELECT * FROM itens_pedido WHERE pedido_id = ?";

        try (PreparedStatement stmtPedido = connection.prepareStatement(sqlPedido);
             PreparedStatement stmtItens = connection.prepareStatement(sqlItens)) {

            stmtPedido.setLong(1, id);
            ResultSet rsPedido = stmtPedido.executeQuery();

            if (!rsPedido.next()) {
                return Optional.empty();
            }

            // Reconstrói o agregado usando factory method
            Pedido pedido = mapearPedido(rsPedido);

            // Busca e adiciona os itens
            stmtItens.setLong(1, id);
            ResultSet rsItens = stmtItens.executeQuery();

            List<ItemPedido> itens = new ArrayList<>();
            while (rsItens.next()) {
                ItemPedido item = mapearItem(rsItens);
                itens.add(item);
            }

            // Usa factory method para restaurar com itens
            pedido = Pedido.restaurarComItens(
                    pedido.getId(),
                    pedido.getClienteId(),
                    pedido.getStatus(),
                    pedido.getValorTotal(),
                    pedido.getDataCriacao(),
                    itens
            );

            return Optional.of(pedido);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido", e);
        }
    }
```
- Vemos aqui que ao buscar um pedido por ID, lidamos com o agregado completo, trazemos o pedido e seus dependentes (ItensPedidos)
- Para lidar com ItemPedido, é necessário mexer com o Agregado (Pedido), ele funciona como unidade única nesse caso, reforçando _**"Não existem ItemPedido sem um Pedido Feito"**_

- No caso da Invariante, garantimos que os métodos que manipulam a classe de domínio não desrespeitem as regras de negócio definidas no domínio
- Desta forma tornando os objetos invariantes, que apesar dos dados internos possam ser diferentes, eles em si são a mesma coisa, pois respeitam a lógica de negócio contida no domínio
- Sendo todos parte de um agregado
- Para exemplificar isso temos:
```java
    // INVARIANTE 1: Pedido deve ter pelo menos 1 item para ser confirmado
    // INVARIANTE 2: Valor total deve ser sempre a soma dos itens
    // INVARIANTE 3: Só pode adicionar itens se status for RASCUNHO
    public void adicionarItem(String produtoId, int quantidade, BigDecimal precoUnitario) {
        if (status != StatusPedido.RASCUNHO) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido " + status);
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        if (precoUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        ItemPedido item = new ItemPedido(produtoId, quantidade, precoUnitario);
        itens.add(item);
        recalcularValorTotal(); // Mantém invariante do valor total
    }
```
- Aqui vemos um método de domínio que contém uma regra de negócio para adição de itens em um pedido
```java
    public void adicionarItemAoPedido(Long pedidoId, String produtoId, int quantidade, BigDecimal preco) {
        // Recupera o agregado completo
        Pedido pedido = repository.buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Modifica através da raiz do agregado (invariantes protegidas)
        pedido.adicionarItem(produtoId, quantidade, preco);

        // Salva o agregado completo
        repository.salvar(pedido);
    }
```
- Na service vemos a invariante sendo aplicada ao adicionar um item ao pedido mantendo a invariante de um objeto, obecendo as regras de negócio.