# Painel do Laboratorio Keycloak

## Visao Geral

Criar um frontend em `frontend/` para evoluir o laboratorio atual de Keycloak. O objetivo e transformar o backend ja feito em uma demo visual, didatica e apresentavel, mantendo a maxima do projeto: reduzir carga cognitiva usando portugues do Brasil em documentacao, telas, codigo, funcoes, arquivos e exemplos sempre que fizer sentido.

A V1 sera um Painel do Laboratorio Keycloak: uma mistura controlada de portal de documentos, painel de diagnostico e admin simples. O usuario deve fazer login real pelo Keycloak, acessar documentos, executar acoes protegidas e ver claramente a relacao entre usuario, token, permissoes, endpoint chamado e resposta HTTP.

## Problema

Hoje o laboratorio ensina bem pelo backend, README, docs e Postman. Isso e otimo para ver o comportamento cru da API, mas ainda exige que o aprendiz conecte mentalmente varias pecas: token, usuario, permissoes, endpoint, status `401`, status `403` e resposta do backend.

Um frontend pode reduzir essa carga cognitiva se ele nao esconder a autorizacao. A interface deve mostrar a acao de negocio e, ao mesmo tempo, explicar o que aconteceu: qual usuario esta logado, quais permissoes ele possui, qual endpoint foi chamado, qual permissao era exigida e por que a resposta foi permitida ou negada.

### Dados de Mercado

APIs seguem sendo parte central do desenvolvimento moderno. O State of API 2025 da Postman indica uso forte de testes de API por times de desenvolvimento, mas ferramentas genericas nao ensinam o dominio especifico do projeto. O diferencial aqui nao e substituir Postman, e sim criar uma experiencia guiada e visual para aprender Keycloak Authorization Services em PT-BR.

## Resumo / Diferencial

O diferencial e ser um laboratorio visual PT-BR-first: nao apenas uma SPA protegida por Keycloak, mas uma tela que torna observavel a cadeia:

`usuario -> token -> permissoes -> endpoint -> resposta HTTP`

## Funcionalidades Principais

| # | Funcionalidade | Prioridade | Descricao |
|---|---|---|---|
| F1 | Frontend em `frontend/` | Critica | Criar a pasta `frontend/` na raiz, mantendo o projeto como monolito modular com backend e frontend separados por modulo. |
| F2 | Codigo PT-BR-first | Critica | Usar nomes em portugues do Brasil em componentes, funcoes, variaveis, arquivos `.js`/`.ts` e textos da UI quando fizer sentido. |
| F3 | Login real via Keycloak | Critica | Permitir entrada e saida usando Keycloak, com fluxo real de autenticacao no navegador. |
| F4 | Client SPA separado | Critica | Adicionar um client publico especifico para o frontend no realm, sem secret no navegador. |
| F5 | Area de documentos | Critica | Listar, ver detalhe, criar, editar e aprovar documentos usando os endpoints reais do backend. |
| F6 | Painel de permissoes | Critica | Mostrar usuario logado, permissoes efetivas e acoes disponiveis ou bloqueadas. |
| F7 | Diagnostico HTTP visivel | Alta | Exibir metodo, endpoint, status e resposta da ultima chamada feita pela UI. |
| F8 | Explicacao de `401` e `403` | Alta | Mostrar mensagens didaticas em PT-BR explicando autenticacao ausente e autorizacao negada. |
| F9 | Demo apresentavel | Alta | Ter uma interface limpa e organizada para demonstrar o fluxo para outra pessoa sem depender so do Postman. |

## KPIs

| KPI | Meta | Como Medir |
|---|---:|---|
| Cobertura dos endpoints principais | 100% | Todos os endpoints atuais de documentos, usuario e publico podem ser acionados ou visualizados pela UI. |
| Cobertura das permissoes | 4 permissoes | A UI demonstra `documentos:ler`, `documentos:criar`, `documentos:editar` e `documentos:aprovar`. |
| Tempo para demonstrar o fluxo | <= 5 minutos | Login, acao permitida e acao negada demonstraveis em sequencia. |
| Clareza de autorizacao | 100% das acoes protegidas | Cada acao mostra permissao exigida, endpoint e status retornado. |
| Consistencia PT-BR | >= 90% | Revisao dos nomes de arquivos, funcoes, componentes, labels e docs do front. |

## Avaliacao da Funcionalidade

| Criterio | Pergunta | Score |
|---|---|---|
| **Impacto** | Quanto isso torna o projeto mais valioso? | Must do |
| **Alcance** | Qual percentual dos usuarios-alvo seria afetado? | Strong |
| **Frequencia** | Com que frequencia o usuario encontra esse valor? | Strong |
| **Diferenciacao** | Isso diferencia ou apenas iguala alternativas? | Strong |
| **Defensibilidade** | E facil copiar ou melhora com o tempo? | Maybe |
| **Viabilidade** | Conseguimos construir isso? | Strong |

Tipo de alavanca: Compounding Feature

## Insights do Council

- **Abordagem recomendada:** construir um painel didatico, nao um portal documental completo.
- **Trade-off principal:** a UI precisa ser bonita o bastante para demo, mas explicita o bastante para nao esconder autorizacao.
- **Risco tecnico:** nao reutilizar client confidential no navegador; criar client SPA publico.
- **Risco pedagogico:** botoes desabilitados nao podem parecer a autorizacao real. O backend continua sendo a fonte da verdade.
- **Objetivo V2+:** evoluir para uma matriz visual usuario x acao x permissao x resultado.

## Fora de Escopo (V1)

- **Admin completo do Keycloak** — Editar realm, policies e permissoes pela UI aumentaria demais o escopo.
- **Gerenciador documental rico** — Filtros avancados, anexos, historico e workflow complexo desviam do aprendizado.
- **Persistencia real dos documentos** — O backend em memoria continua suficiente para o laboratorio.
- **Exibicao de token bruto como foco principal** — Claims relevantes podem aparecer, mas sem incentivar copiar tokens.
- **Internacionalizacao** — A V1 e PT-BR-first por decisao do projeto.

## Architecture Decision Records

- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — Define a V1 como painel didatico, com documentos reais, diagnostico HTTP e client SPA proprio.

## Perguntas em Aberto

- A stack do frontend sera React, Vue, Angular ou outra?
- O front deve usar JavaScript ou TypeScript?
- O Docker Compose deve subir `frontend` junto com `backend` e `keycloak` ja na V1?
- O realm atual deve ser alterado para incluir o client SPA no mesmo arquivo JSON?
- O painel deve mostrar claims completas ou apenas claims filtradas e explicadas?
