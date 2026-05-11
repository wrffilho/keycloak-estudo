# PRD: Menu Dinamico Pelo Keycloak

## Visao Geral

O Menu Dinamico Pelo Keycloak transforma o frontend do Laboratorio Keycloak em uma experiencia mais proxima de um sistema corporativo real. Apos o login, o usuario vera apenas os menus e submenus liberados para ele pelo Keycloak.

O recurso e voltado para desenvolvedores brasileiros que querem entender, na pratica, como autenticacao, catalogo de navegacao e autorizacao se relacionam sem confundir menu de frontend com seguranca de endpoint backend.

A V1 deve demonstrar tres ideias centrais: o frontend autentica o usuario, o backend consulta o Keycloak para obter a navegacao permitida, e o Vue renderiza menus e rotas reais conforme o resultado recebido.

## Objetivos

- Demonstrar menus diferentes para pelo menos 3 usuarios atuais e 1 ou 2 usuarios ficticios.
- Exibir pelo menos 4 grupos de menu corporativo: Cadastros, Operacoes, Relatorios e Administracao.
- Incluir pelo menos 3 menus raiz e 6 submenus.
- Permitir navegacao real no Vue Router para rotas ficticias.
- Mostrar uma tela simples de acesso permitido quando o usuario abrir uma rota liberada.
- Mostrar alerta fixo de acesso negado e redirecionar para `/laboratorio` quando o usuario acessar diretamente uma rota nao liberada.
- Manter todo texto de interface e documentacao em portugues do Brasil.

## Historias De Usuario

Como usuario leitor, quero ver somente menus compativeis com meu perfil para entender que minha navegacao e limitada pelo Keycloak.

Como usuario editor, quero ver menus adicionais em relacao ao leitor para comparar permissoes e perceber a diferenca de experiencia.

Como usuario aprovador, quero ver uma arvore diferente da do editor para entender que permissoes nao sao apenas “mais ou menos acesso”, mas combinacoes diferentes.

Como usuario ficticio administrativo, quero acessar menus como Administracao ou Relatorios para simular um sistema maior.

Como desenvolvedor estudando Keycloak, quero clicar em menus ficticios e ver uma tela simples dizendo que meu usuario tem acesso aquela tela.

Como desenvolvedor estudando acesso negado, quero digitar uma rota nao liberada e ver uma mensagem clara antes de voltar para a home do laboratorio.

## Funcionalidades Principais

| # | Funcionalidade | Prioridade | Descricao |
| --- | --- | --- | --- |
| F1 | Catalogo de menu no Keycloak | Critica | O catalogo de menus e submenus deve ser administrado no Keycloak, separado do menu fixo do frontend. |
| F2 | Client dedicado de menu | Critica | A experiencia deve usar um client confidencial dedicado ao catalogo de navegacao, separado do client publico da SPA e do client da API. |
| F3 | Carregamento de menu apos login | Critica | Apos autenticacao, o frontend deve carregar a arvore de menus permitida para o usuario logado. |
| F4 | Menu corporativo ficticio | Alta | A V1 deve simular um sistema maior com Cadastros, Operacoes, Relatorios e Administracao. |
| F5 | Rotas ficticias funcionais | Alta | Os menus ficticios devem navegar para rotas reais do Vue Router, mesmo que apontem para uma tela compartilhada simples. |
| F6 | Tela de acesso permitido | Alta | Ao abrir uma rota liberada, a tela deve informar: `Usuario {nome} acessou {rotulo} porque o menu foi liberado pelo Keycloak.` |
| F7 | Acesso negado em rota direta | Alta | Se o usuario acessar diretamente uma rota nao liberada, deve ver alerta fixo estilo Bootstrap e retornar para `/laboratorio` ao fechar ou confirmar. |
| F8 | Separacao didatica | Alta | A experiencia deve deixar claro que menu controla navegacao, enquanto endpoints continuam protegidos separadamente. |

## Experiencia Do Usuario

1. O usuario abre o frontend.
2. O usuario faz login pelo Keycloak.
3. O frontend carrega o menu permitido.
4. O cabecalho deixa de exibir links fixos e passa a renderizar a arvore recebida.
5. O usuario ve menus e submenus compativeis com seu perfil.
6. Ao clicar em uma rota ficticia liberada, ele navega para uma pagina simples de confirmacao de acesso.
7. Ao digitar manualmente uma rota ficticia sem acesso, ele ve um alerta fixo de acesso negado.
8. Ao fechar ou confirmar o alerta, o usuario volta para `/laboratorio`.

A experiencia deve ser didatica, direta e sem aparencia de produto inacabado. Menus ficticios devem ser identificados pelo contexto da mensagem, nao por avisos longos ou explicacoes excessivas na tela.

## Restricoes De Alto Nivel

- O frontend nao deve decidir sozinho quais menus existem para cada usuario.
- O frontend nao deve ler claims do token para montar a navegacao.
- O menu nao deve substituir autorizacao real de endpoints.
- O catalogo de menu deve vir do Keycloak.
- O client publico da SPA deve continuar focado em login.
- O client da API deve continuar focado em protecao dos endpoints.
- O catalogo de navegacao deve ficar em client confidencial dedicado.
- A interface e os artefatos devem seguir PT-BR-first.

## Fora Do Escopo

- Editor visual de menu.
- Implementar telas reais para todos os menus ficticios.
- Internacionalizacao alem de portugues do Brasil.
- Cache avancado de menu.
- Substituir autorizacao dos endpoints por controle de menu.
- Frontend consultar Keycloak diretamente para montar navegacao.
- Criar um portal corporativo completo com muitos perfis e modulos.

## Plano De Entrega

### MVP

- Criar catalogo inicial de menus e submenus.
- Demonstrar usuarios atuais e 1 ou 2 usuarios ficticios com menus diferentes.
- Renderizar menu dinamico no cabecalho.
- Criar rotas ficticias funcionais.
- Exibir tela simples de acesso permitido.
- Exibir alerta de acesso negado em rota direta sem permissao.

Criterio para avancar: fluxo login -> menu permitido -> navegacao -> acesso negado deve funcionar localmente para usuarios diferentes.

### Fase 2

- Melhorar a visualizacao didatica mostrando por que cada menu foi liberado.
- Refinar a organizacao dos menus ficticios.
- Adicionar mais combinacoes de usuarios/perfis se isso ajudar o aprendizado.

### Fase 3

- Avaliar editor administrativo de menu.
- Avaliar cache controlado por sessao.
- Avaliar tela de auditoria didatica para decisoes de menu.

## Metricas De Sucesso

| Metrica | Meta |
| --- | --- |
| Usuarios com menus diferentes | Pelo menos 4 usuarios demonstraveis |
| Estrutura de menu | Pelo menos 3 menus raiz e 6 submenus |
| Rotas ficticias funcionais | 100% dos menus ficticios navegam para uma rota real |
| Acesso negado direto | 100% das rotas nao liberadas exibem alerta e retornam para `/laboratorio` |
| Acoplamento do frontend ao Keycloak | 0 leitura direta de claims para montar menu |
| Clareza didatica | Usuario consegue entender que menu nao substitui autorizacao de endpoint |

## Riscos E Mitigacoes

| Risco | Mitigacao |
| --- | --- |
| Usuario confundir menu com seguranca | Mensagens e documentacao devem explicar que endpoints continuam protegidos separadamente. |
| Menus ficticios parecerem funcionalidades quebradas | Rotas ficticias devem abrir tela simples de confirmacao de acesso, nao paginas vazias. |
| Escopo crescer demais | Limitar V1 a poucos grupos representativos e 1 ou 2 usuarios ficticios. |
| Modelo ficar abstrato demais | Usar nomes concretos como Cadastros, Operacoes, Relatorios e Administracao. |
| Acesso negado frustrar o usuario | Mostrar alerta objetivo e redirecionar para `/laboratorio` de forma previsivel. |

## Architecture Decision Records

- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — Decide criar um client confidencial dedicado ao catalogo de menu/submenu.
- [ADR-002: Menu Corporativo Didatico Com Controle De Rota](adrs/adr-002.md) — Decide usar menus corporativos ficticios, rotas reais no Vue Router e alerta de acesso negado.

## Questoes Em Aberto

- Quais nomes finais serao usados para os 1 ou 2 usuarios ficticios.
- Quais submenus exatos entram em Cadastros, Operacoes, Relatorios e Administracao.
- Qual texto final do alerta de acesso negado.
- Qual texto final da tela de acesso permitido.
- Como a documentacao vai explicar a diferenca entre menu, rota frontend e endpoint backend.
