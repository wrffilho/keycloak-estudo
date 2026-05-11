# Menu dinamico com Keycloak

Este roteiro explica o menu dinamico do frontend. Ele foi criado para demonstrar uma separacao importante:

- **menu** mostra a navegacao que o usuario pode enxergar;
- **rota frontend** abre uma tela no Vue Router;
- **endpoint backend** continua protegido pela propria autorizacao da API.

Menu nao e seguranca de backend. Mesmo que um link nao apareca, uma chamada direta a um endpoint protegido ainda precisa ser validada pelo Spring Security.

## Clients envolvidos

| Client | Papel |
|---|---|
| `laboratorio-frontend` | Login da SPA Vue. |
| `laboratorio-api` | Protecao dos endpoints do backend. |
| `laboratorio-menu` | Catalogo de menus e submenus usados pelo frontend. |

O frontend chama o backend em `GET /usuario/menu`. O backend consulta o Keycloak, le o catalogo do client `laboratorio-menu`, filtra os itens conforme o usuario logado e devolve uma arvore pronta para o Vue renderizar.

## Usuarios de teste

Todos usam a senha `senha123`.

| Usuario | Exemplo de experiencia |
|---|---|
| `leitor` | Ve operacoes basicas, como Documentos e Tarefas. |
| `editor` | Ve Cadastros e Operacoes, incluindo Clientes, Fornecedores e Documentos. |
| `aprovador` | Ve Operacoes e Relatorios, incluindo Aprovacoes e Gerenciais. |
| `gestor` | Ve Cadastros, Operacoes e Relatorios. |
| `administrador` | Ve Cadastros, Operacoes, Relatorios e Administracao. |

## Menus ficticios

O laboratorio inclui menus de um sistema maior para testar hierarquia e visibilidade:

- Cadastros
  - Clientes
  - Fornecedores
- Operacoes
  - Documentos
  - Aprovacoes
  - Tarefas
- Relatorios
  - Gerenciais
  - Produtividade
- Administracao
  - Usuarios
  - Perfis

Nem todas essas telas representam funcionalidades reais. As rotas existem no Vue Router para simular navegacao real. Quando o usuario tem acesso, a tela mostra uma mensagem como:

```text
Usuario editor acessou Clientes porque o menu foi liberado pelo Keycloak.
```

## Teste manual

1. Suba o ambiente:

```bash
docker compose down
docker compose up -d --build
```

2. Acesse o frontend:

```text
http://localhost:5173
```

3. Entre com `leitor`, `editor`, `aprovador`, `gestor` ou `administrador`.

4. Observe que o menu muda conforme o usuario.

5. Clique em uma rota ficticia permitida, como um submenu que aparece para o usuario logado. A tela deve confirmar que o usuario tem acesso.

6. Digite manualmente uma rota que nao aparece para o usuario, por exemplo:

```text
http://localhost:5173/tela/administracao-usuarios
```

Se o usuario nao tiver acesso, o frontend deve mostrar um alerta fixo de acesso negado. Ao confirmar o alerta, a aplicacao volta para `/laboratorio`.

## Como pensar sobre acesso

Uma rota frontend pode carregar varios dados do backend. Por exemplo, uma tela `/x` pode buscar dados em `/y`, `/z` e `/w`. Por isso, liberar uma rota no menu nao significa liberar automaticamente todos os endpoints usados pela tela.

O menu dinamico melhora a experiencia e ensina o efeito das permissoes na navegacao. A autorizacao real continua em cada endpoint backend.
