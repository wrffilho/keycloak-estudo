# Laboratorio de Autorizacao com Keycloak

## Visao Geral

Este projeto e um laboratorio de aprendizado em portugues do Brasil para estudar Keycloak Authorization Services por meio de uma aplicacao demo executavel. Ele deve ajudar um desenvolvedor brasileiro a entender, na pratica, a diferenca entre endpoints publicos, endpoints autenticados e endpoints protegidos por autorizacao fina.

A V1 sera uma demo backend-first, sem frontend, com Keycloak subindo via Docker Compose, API organizada como monolito modular, configuracao inicial do Keycloak e exemplos de teste pelo Postman. O foco e tornar visivel o caminho entre URI do endpoint, recurso, escopo, politica, permissao e resultado HTTP.

O projeto e valioso porque autorizacao no Keycloak costuma ficar confusa quando estudada apenas pela documentacao. A demo deve permitir que cada acesso permitido ou negado seja explicado em portugues, com nomes de classes, funcoes, exemplos e documentacao voltados para quem esta aprendendo na propria lingua nativa.

## Problema

Um desenvolvedor iniciando com Keycloak normalmente entende login antes de entender autorizacao. A confusao aparece quando entram recursos, escopos, politicas, permissoes, modos de enforcement e protecao baseada em URI. Sem um exemplo executavel, fica dificil separar "o usuario esta autenticado" de "o usuario tem permissao para acessar este endpoint".

O usuario esta com o ambiente local zerado, entao o projeto nao deve assumir realm, client, usuarios, Docker, frontend ou configuracao previa do Keycloak. A experiencia precisa comecar do zero e levar ate chamadas reais no Postman, mostrando `200`, `401` e `403` em cenarios bem explicados.

O laboratorio tambem precisa reduzir carga cognitiva. Por isso, nomes em portugues do Brasil devem ser usados sempre que fizer sentido em classes, funcoes, endpoints, exemplos, comentarios e documentacao. Termos tecnicos do Keycloak devem ser explicados com consistencia: recurso, escopo, politica, permissao, autenticado, autorizado e negado.

### Dados de Mercado

Identity and Access Management continua sendo uma area profissionalmente relevante. A Grand View Research estima o mercado global de IAM em USD 26.77B em 2025, com projecao de USD 62.90B ate 2033 e CAGR de 11.3%. Aprender autorizacao com Keycloak tem valor pratico porque Keycloak e uma plataforma open-source amplamente usada para IAM.

## Resumo / Diferencial

O diferencial e ser um laboratorio PT-BR-first de Keycloak Authorization Services que comeca do zero e ensina por comportamento de endpoint. Em vez de esconder autorizacao atras de uma interface grafica, a V1 expoe o ciclo cru de aprendizado pelo Postman: obter token, chamar endpoint, observar `200`, `401` ou `403`, e conectar esse resultado a recurso, escopo, politica e permissao no Keycloak.

## Funcionalidades Principais

| # | Funcionalidade | Prioridade | Descricao |
|---|---|---|---|
| F1 | Keycloak via Docker Compose | Critica | Fornecer Docker Compose para subir o Keycloak localmente a partir de um ambiente zerado. |
| F2 | API em monolito modular | Critica | Criar uma API backend organizada como monolito modular, com nomes em PT-BR quando fizer sentido e caminho aberto para frontend futuro. |
| F3 | Endpoints publicos | Critica | Incluir endpoints que nao exigem autenticacao para demonstrar rotas intencionalmente livres. |
| F4 | Endpoints autenticados | Critica | Incluir endpoints que exigem login valido, mas nao exigem autorizacao fina por recurso/politica/permissao. |
| F5 | Endpoints autorizados | Critica | Incluir endpoints mapeados para recursos, escopos, politicas, permissoes e URIs no Keycloak. |
| F6 | Cenarios de acesso negado | Alta | Incluir usuarios de exemplo em que um usuario autenticado recebe `403` por falta de permissao. |
| F7 | Colecao Postman de aprendizado | Alta | Fornecer exemplos para obter token e chamar endpoints com resultados esperados `200`, `401` e `403`. |
| F8 | Documentacao didatica em PT-BR | Alta | Explicar recurso, escopo, politica, permissao, URI, token, autenticacao, autorizacao e acesso negado. |

## KPIs

| KPI | Meta | Como Medir |
|---|---:|---|
| Tempo para rodar o laboratorio local | <= 20 minutos | Seguir o README em um ambiente limpo e registrar o tempo de setup. |
| Cobertura de endpoints | >= 8 endpoints | Contar endpoints publicos, autenticados, autorizados e de cenario negado. |
| Categorias de autorizacao demonstradas | >= 4 categorias | Verificar fluxos publico, sem autenticacao `401`, sem permissao `403` e autorizado `200`. |
| Exemplos no Postman | >= 10 requisicoes | Contar requisicoes da colecao, incluindo token e casos negativos. |
| Conceitos explicados em PT-BR | >= 8 conceitos | Confirmar documentacao de recurso, escopo, politica, permissao, URI, token, autenticado e autorizado/negado. |
| Exercicio de permissao independente | >= 1 exercicio | Incluir tarefa final em que o aprendiz altera ou cria uma permissao e valida o comportamento. |

## Avaliacao da Funcionalidade

| Criterio | Pergunta | Score |
|---|---|---|
| **Impacto** | Quanto isso torna o projeto mais valioso? | Strong |
| **Alcance** | Qual percentual dos usuarios-alvo seria afetado? | Strong |
| **Frequencia** | Com que frequencia o usuario encontra esse valor? | Strong |
| **Diferenciacao** | Isso diferencia ou apenas iguala alternativas? | Strong |
| **Defensibilidade** | E facil copiar ou melhora com o tempo? | Maybe |
| **Viabilidade** | Conseguimos construir isso? | Must do |

Tipo de alavanca: Quick Win

## Insights do Council

- **Abordagem recomendada:** criar um laboratorio pequeno, executavel e em PT-BR com Keycloak, API backend, Docker Compose e exemplos pelo Postman.
- **Principais trade-offs:** manter a V1 estreita o suficiente para terminar; evitar frontend, persistencia pesada, multiplos servicos e excesso de tipos de politica antes de consolidar o modelo mental.
- **Riscos identificados:** o aprendiz pode confundir autenticacao com autorizacao; configuracoes permissivas podem ensinar habitos inseguros; escopo grande demais pode esconder a cadeia recurso/escopo/politica/permissao.
- **Objetivo estendido para V2+:** adicionar frontend consumindo a mesma API de monolito modular depois que o aprendizado via Postman estiver claro.

## Fora de Escopo (V1)

- **Frontend** — Fica para depois porque a V1 deve focar no comportamento cru da autorizacao via Postman.
- **Hardening de producao** — TLS, gestao real de segredos, hardening de admin e isolamento de rede ficam fora de um laboratorio local inicial.
- **Multiplos microservicos** — Monolito modular e suficiente para aprender autorizacao por endpoint sem aumentar a complexidade operacional.
- **Catalogo avancado de politicas** — A V1 nao precisa cobrir todos os tipos de politica do Keycloak; deve ensinar bem a cadeia principal primeiro.
- **Fluxo de negocio com banco pesado** — Persistencia nao deve distrair dos conceitos de autorizacao, salvo se for realmente necessaria para exemplos simples.

## Architecture Decision Records

- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — Define uma V1 backend-first, dockerizada, testada via Postman e com frontend adiado para V2+.

## Perguntas em Aberto

- O dominio principal da demo deve ser `documentos`, `relatorios` ou outro dominio em PT-BR?
- Qual stack backend sera usada na implementacao?
- O realm do Keycloak deve ser importado automaticamente ou configurado passo a passo primeiro para fins didaticos?
- Quantos usuarios de exemplo a V1 deve incluir?
- Quais endpoints exatos devem compor o primeiro roteiro de testes no Postman?
