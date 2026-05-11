# Menu Dinamico Pelo Keycloak

## Visao Geral

Criar um menu dinamico no frontend do Laboratorio Keycloak, carregado apos o login do usuario. O frontend nao tera menu fixo nem decidira sozinho quais rotas exibir. Ele chamara um endpoint autenticado do backend, e o backend consultara o Keycloak para montar a arvore de menu/submenu permitida para o usuario logado.

A V1 tera tres clients no Keycloak: `laboratorio-frontend` para login da SPA, `laboratorio-api` para proteger endpoints do backend e `laboratorio-menu` como client confidencial dedicado ao catalogo de navegacao do frontend.

Para tornar o teste mais realista, a V1 deve incluir alguns menus e submenus ficticios, simulando um sistema maior. Esses itens servem para validar hierarquia, ordenacao, visibilidade por usuario e renderizacao no frontend, mesmo que nem todas as telas ou endpoints existam ainda. As rotas ficticias devem existir no Vue Router para simular uma navegacao real. Ao clicar em um menu ficticio, o frontend deve navegar para a rota correspondente e abrir uma tela didatica simples informando que o usuario logado tem acesso a essa tela.

## Problema

O projeto precisa demonstrar uma situacao realista: a navegacao do frontend muda conforme o usuario logado. Porem, rota de frontend nao e a mesma coisa que permissao de endpoint. Uma tela pode carregar varios dados internos, combos e acoes de backend, cada um com autorizacao propria.

Usar `/usuario/permissoes` para montar menu seria limitado, porque trataria permissoes de API como se fossem rotas da interface. A proposta correta para aprendizado e cadastrar o catalogo de menu/submenu no Keycloak e deixar o backend atuar como adaptador entre Keycloak e Vue.

### Dados De Mercado E Tecnica

Sistemas corporativos normalmente separam autenticacao, autorizacao de API e experiencia de navegacao. A documentacao oficial do Keycloak descreve `resources`, `scopes`, `permissions`, `policies`, `URIs` e `attributes`, que podem ser usados para modelar recursos protegidos e metadados de navegacao: https://www.keycloak.org/docs/latest/authorization_services/index.html

## Funcionalidades Principais

| # | Funcionalidade | Prioridade | Descricao |
| --- | --- | --- | --- |
| F1 | Client dedicado de menu | Critica | Criar `laboratorio-menu` como client confidencial para catalogar menus/submenus do frontend. |
| F2 | Cadastro de rotas no Keycloak | Critica | Representar itens de menu com rota, rotulo, ordem, icone, tipo e relacao pai/filho. |
| F3 | Endpoint `/usuario/menu` | Critica | Backend consulta o Keycloak, filtra o catalogo para o usuario logado e devolve JSON pronto. |
| F4 | Renderizacao dinamica no Vue | Alta | Frontend renderiza o menu recebido e deixa de depender de links fixos no cabecalho. |
| F5 | Menus ficticios de sistema maior | Alta | Criar itens como Cadastros, Relatorios, Administracao e Operacoes para testar hierarquia e permissoes sem implementar todo o produto. |
| F6 | Rotas ficticias funcionais | Alta | Cadastrar rotas no Vue Router para os menus ficticios, todas apontando para uma pagina simples compartilhada de confirmacao de acesso. |
| F7 | Placeholder didatico de acesso | Alta | Ao navegar para uma rota ficticia, exibir mensagem como `Usuario {nome} tem acesso a tela {rotulo}`. |
| F8 | Separacao didatica | Alta | Documentar que menu e experiencia de navegacao, enquanto endpoints seguem protegidos no backend. |

## KPIs

| KPI | Meta | Como Medir |
| --- | --- | --- |
| Menus diferentes por usuario | 3 usuarios com arvores diferentes | Teste manual/local com usuarios do realm |
| Profundidade de navegacao | Pelo menos 3 menus raiz e 6 submenus | Verificacao do JSON retornado por `/usuario/menu` |
| Tempo de carregamento do menu | < 700 ms local | Medicao no navegador ou teste de integracao |
| Acoplamento do frontend ao Keycloak | 0 leitura direta de claims para montar menu | Revisao de codigo |
| Seguranca dos endpoints | 100% dos endpoints protegidos continuam com autorizacao propria | Testes backend existentes/novos |

## Avaliacao Da Funcionalidade

| Criterio | Pergunta | Score |
| --- | --- | --- |
| **Impacto** | Quanto isso torna o produto mais valioso? | Strong |
| **Alcance** | Qual percentual de usuarios seria afetado? | Strong |
| **Frequencia** | Com que frequencia o usuario encontra esse valor? | Strong |
| **Diferenciacao** | Isso diferencia ou apenas iguala concorrentes? | Strong para laboratorio |
| **Defensibilidade** | E facil copiar ou melhora com o tempo? | Maybe |
| **Viabilidade** | Conseguimos construir? | Strong |

Tipo de alavancagem: funcionalidade didatica e arquitetural.

## Insights Do Council

- **Abordagem recomendada:** criar um client confidencial dedicado a menu, consultado pelo backend apos login, para devolver uma arvore de navegacao pronta ao frontend.
- **Trade-offs principais:** mais configuracao no Keycloak em troca de separacao correta entre login, API e catalogo de navegacao; mais investigacao tecnica em troca de aprendizado mais fiel.
- **Riscos identificados:** confundir menu com seguranca, dar privilegios demais ao backend para consultar Keycloak e acoplar o backend a detalhes da API administrativa.
- **Mitigacoes:** manter endpoints protegidos separadamente, usar service account com menor privilegio possivel e isolar a integracao em um servico backend dedicado.
- **Meta V2+:** criar uma tela didatica que mostre como cada item do menu foi liberado pelo Keycloak.

## Fora Do Escopo V1

- **Editor visual de menu** — a V1 pode cadastrar os recursos direto no realm/importacao do Keycloak.
- **Internacionalizacao alem de PT-BR** — o projeto prioriza portugues do Brasil para reduzir carga cognitiva.
- **Cache sofisticado de menu** — primeiro validar o fluxo ponta a ponta sem introduzir invalidez de cache.
- **Usar menu para autorizar chamadas de API** — endpoints continuam protegidos por autorizacao propria no backend.
- **Frontend consultar Keycloak diretamente para montar navegacao** — o backend deve ser o adaptador entre Keycloak e Vue.
- **Implementar telas reais para os menus ficticios** — menus de sistema maior devem ter rotas reais no Vue Router, mas podem apontar para uma pagina compartilhada simples que confirma o acesso do usuario a tela selecionada.

## Architecture Decision Records

- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — Decide criar um client confidencial dedicado ao catalogo de menu/submenu, consultado pelo backend apos login.

## Questoes Em Aberto

- Qual API exata do Keycloak sera usada para listar resources/attributes do `laboratorio-menu`.
- Como avaliar permissoes do usuario contra os resources do client de menu.
- Qual formato final do JSON de menu para o Vue.
- Quais usuarios/perfis iniciais demonstrarao menus diferentes.
- Quais menus ficticios farao parte da massa inicial do laboratorio.
- Qual texto final sera usado no placeholder didatico de rotas ficticias.
