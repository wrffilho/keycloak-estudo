# Council Prompt: Painel do Laboratorio Keycloak

Contexto do projeto:

- Projeto: Laboratorio de Autorizacao com Keycloak.
- Maxima do projeto: reduzir carga cognitiva usando portugues do Brasil em nomes, telas, documentacao, exemplos e explicacoes.
- Backend ja implementado como monolito modular Spring Boot em `backend/`.
- Dominio atual: `documentos`.
- Usuarios de estudo: `leitor`, `editor`, `aprovador`.
- Permissoes: `documentos:ler`, `documentos:criar`, `documentos:editar`, `documentos:aprovar`.
- Endpoints existentes:
  - `GET /publico/status`
  - `GET /publico/sobre`
  - `GET /usuario/perfil`
  - `GET /usuario/permissoes`
  - `GET /documentos`
  - `GET /documentos/{id}`
  - `POST /documentos`
  - `PUT /documentos/{id}`
  - `POST /documentos/{id}/aprovar`

Ideia refinada:

Criar um frontend para ajudar a testar e demonstrar o backend. O frontend deve ser uma demo completa, com base em um "Painel do Laboratorio", mas incorporando um pouco de portal de documentos e um pouco de admin simples.

Decisoes ja tomadas com o usuario:

- Estilo: painel de laboratorio com elementos de portal de documentos e admin simples.
- Login: login real via Keycloak, nao apenas simulacao.
- Documentos: V1 deve permitir listar, ver detalhe, criar, editar e aprovar, respeitando permissoes.
- Sucesso: deve servir para aprendizado claro, teste rapido e demo apresentavel.
- Idioma: tudo em portugues do Brasil para manter a maxima do projeto.

Pesquisa consolidada:

- Keycloak recomenda `keycloak-js` para proteger aplicacoes web e usar Authorization Code flow com PKCE.
- Apps client-side devem usar public client e redirect URIs especificas; o client atual `laboratorio-api` e confidential client com secret, entao provavelmente a V1 precisa de um client SPA separado no realm.
- Browser-based OAuth deve evitar expor client secret no navegador.
- O diferencial nao e substituir Postman genericamente, e sim tornar visivel a relacao entre usuario, token, permissao, endpoint e resposta HTTP (`200`, `201`, `401`, `403`).

Dilema para o council:

Qual deve ser o escopo certo da V1 do frontend para maximizar aprendizado e demo, sem transformar o laboratorio em um produto grande demais?

Analise estes pontos:

1. Scope: a V1 deve incluir CRUD completo de documentos, painel de token/claims/permissoes e chamadas HTTP visiveis?
2. Prioridade: isso deve ser tratado como V2 natural do laboratorio backend-first ou dividido em etapas menores?
3. Tecnica: login real via Keycloak deve exigir client SPA separado no realm desde a V1?
4. Riscos: o front pode esconder demais a autorizacao ou ensinar habitos inseguros?
5. 10x challenge: existe uma versao mais simples ou mais poderosa que ensine melhor?

Entregue sua abertura em 2-3 paragrafos e termine com uma linha `Key Point: ...`.
