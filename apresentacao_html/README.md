#  Apresentação HTML - Padrões de Projeto

## 🗂️ Estrutura do Projeto

```
apresentacao_html/
├── README.md           # Este arquivo
├── index.html          # Arquivo principal da apresentação
├── slides/             # HTML de cada slide individual
│   ├── slide01.html    # Slide 1: Título + Agenda
│   ├── slide02.html    # Slide 2: Questão 1 - Introdução
│   ├── slide03.html    # Slide 3: DAO
│   ├── ...             # Slides 4-21
│   └── slide21.html    # Slide 21: Síntese Final
├── assets/             # Recursos da apresentação
│   ├── css/           # Estilos CSS
│   ├── js/            # Scripts JavaScript
│   └── images/        # Imagens e diagramas
└── marp/              # Versão Marp (opcional)
```

## 🚀 Como usar:

### Opção 1: Reveal.js
1. Baixar Reveal.js: https://revealjs.com/
2. Colocar na pasta `assets/`
3. Usar `index.html` como base

### Opção 2: Marp
1. Instalar: `npm install -g @marp-team/marp-cli`
2. Usar arquivo na pasta `marp/`
3. Gerar: `marp apresentacao.md --html`

### Opção 3: HTML puro
1. Usar slides individuais da pasta `slides/`
2. Navegar manualmente ou criar navegação JS

## 📝 Próximos passos:

1. ✅ Estrutura criada
2. ⏳ Criar slides individuais
3. ⏳ Configurar biblioteca de apresentação
4. ⏳ Adicionar estilos CSS
5. ⏳ Testar apresentação

## 🎯 Baseado nas 4 Questões:

1. **🗃️ Questão 1:** Padrões de Persistência (DAO, Data Mapper, Repository)
2. **🔄 Questão 2:** Refatorações (Strategy e Decorator)  
3. **💉 Questão 3:** Declínio GOF vs Ascensão DI
4. **👁️ Questão 4:** Observer Onipresente