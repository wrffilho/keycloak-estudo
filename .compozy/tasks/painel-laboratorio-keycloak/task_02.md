---
status: completed
title: Criar modulo frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable
type: frontend
complexity: medium
dependencies:
  - task_01
---

# Task 02: Criar modulo frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable

## Overview
Esta tarefa cria a base do modulo `frontend/` na raiz do repositorio. Ela estabelece a stack escolhida, a estrutura inicial de pastas, as rotas basicas e a base visual GetDesign Lovable para que as proximas tarefas possam implementar autenticacao, permissoes e documentos.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST criar `frontend/` com Vue, TypeScript, Vite e Vue Router.
- MUST executar e integrar a base visual `npx getdesign@latest add lovable`.
- MUST criar estrutura inicial em PT-BR para `componentes`, `composables`, `servicos`, `tipos`, `paginas` e `rotas`.
- MUST configurar rotas iniciais `/`, `/laboratorio`, `/documentos` e `/documentos/:id`.
- SHOULD manter nomes de dominio em portugues do Brasil.
</requirements>

## Subtasks
- [ ] 2.1 Criar projeto base dentro de `frontend/`.
- [ ] 2.2 Adicionar Vue Router e rotas iniciais.
- [ ] 2.3 Aplicar base visual GetDesign Lovable.
- [ ] 2.4 Criar estrutura de pastas definida na TechSpec.
- [ ] 2.5 Adicionar testes iniciais de renderizacao e roteamento.

## Implementation Details
Seguir a TechSpec nas secoes "Frontend Structure", "Frontend Routes" e "Integration Points > GetDesign Lovable". Esta tarefa nao deve implementar login real nem chamadas ao backend; ela cria a fundacao para as tarefas seguintes.

### Relevant Files
- `.compozy/tasks/painel-laboratorio-keycloak/_techspec.md` — define stack, estrutura e rotas.
- `package.json` na raiz — verificar se existe configuracao global antes de criar scripts locais.
- `docker-compose.yml` — sera alterado em tarefa posterior.

### Dependent Files
- `frontend/package.json` — scripts e dependencias do frontend.
- `frontend/src/main.ts` — entrada da SPA.
- `frontend/src/App.vue` — shell inicial da aplicacao.
- `frontend/src/rotas/rotas.ts` — definicao das rotas do laboratorio.
- `frontend/src/paginas/*.vue` — paginas iniciais vazias ou placeholders.

### Related ADRs
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — define stack e base visual.
- [ADR-004: Organizacao do Frontend por Composables e Servicos de Dominio](adrs/adr-004.md) — orienta estrutura modular do frontend.

## Deliverables
- Modulo `frontend/` criado e executavel localmente.
- Rotas basicas configuradas com Vue Router.
- Base visual GetDesign Lovable aplicada e adaptada ao laboratorio.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for initial routing behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] `App.vue` renderiza sem erro.
  - [ ] Pagina `/laboratorio` renderiza o titulo do painel.
  - [ ] Pagina `/documentos` renderiza placeholder de documentos.
- Integration tests:
  - [ ] Rota `/` redireciona para `/laboratorio`.
  - [ ] Rota `/documentos/:id` aceita parametro de documento.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- `frontend/` possui stack Vue + TypeScript + Vite + Vue Router.
- Estrutura inicial segue nomes PT-BR-first.
