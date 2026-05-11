---
status: completed
title: Implementar rotas ficticias e tela de acesso permitido
type: frontend
complexity: medium
dependencies:
  - task_05
---

# Task 07: Implementar rotas ficticias e tela de acesso permitido

## Overview
Esta tarefa adiciona navegacao real para menus ficticios por meio de rota generica e pagina compartilhada. Ela permite simular um sistema maior sem implementar todos os modulos.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST add a generic route for fictional menu screens.
- MUST keep existing explicit routes for real pages.
- MUST show the message `Usuario {nome} acessou {rotulo} porque o menu foi liberado pelo Keycloak.` for permitted routes.
- MUST resolve screen label from the dynamic menu state.
- SHOULD show a sensible fallback when label is unavailable.
</requirements>

## Subtasks
- [x] 7.1 Add generic route for fictional screens.
- [x] 7.2 Create shared page for permitted fictional access.
- [x] 7.3 Resolve user and menu label for the page message.
- [x] 7.4 Preserve existing `laboratorio` and `documentos` routes.
- [x] 7.5 Add route/page tests.

## Implementation Details
Modify `frontend/src/rotas/rotas.ts` and create a page under `frontend/src/paginas/`. Use `useSessao` for username and `useMenuDinamico` for route label lookup.

### Relevant Files
- `frontend/src/rotas/rotas.ts` — central Vue Router registration.
- `frontend/src/paginas/PainelDoLaboratorio.vue` — existing page style reference.
- `frontend/src/composables/useSessao.ts` — username source.
- `frontend/src/composables/useMenuDinamico.ts` — expected new menu lookup state from task 05.

### Dependent Files
- `frontend/src/componentes/layout/CabecalhoPrincipal.vue` — dynamic links from task 06 navigate to these routes.

### Related ADRs
- [ADR-002: Menu Corporativo Didatico Com Controle De Rota](adrs/adr-002.md) — requires routes and permitted-access page.
- [ADR-005: Composable Simples E Rotas Hibridas No Frontend](adrs/adr-005.md) — defines hybrid routing approach.

## Deliverables
- Generic fictional route.
- Shared permitted-access page.
- Tests for message and route behavior.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for Vue Router route resolution **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Page renders username and menu label for a known route.
  - [x] Page renders fallback label for unknown route before guard task.
  - [x] Message text matches PRD wording.
- Integration tests:
  - [x] Vue Router resolves an example fictional route to the shared page.
  - [x] Existing `/laboratorio` and `/documentos` routes still resolve.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Fictional menu links navigate to a real Vue route and show permitted-access confirmation.
