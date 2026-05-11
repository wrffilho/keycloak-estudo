---
status: completed
title: Implementar bloqueio de rota direta e alerta de acesso negado
type: frontend
complexity: high
dependencies:
  - task_06
  - task_07
---

# Task 08: Implementar bloqueio de rota direta e alerta de acesso negado

## Overview
Esta tarefa protege a experiencia de navegacao quando o usuario digita diretamente uma rota ficticia que nao esta no menu permitido. Ela deve mostrar um alerta fixo de acesso negado e voltar para `/laboratorio` quando o usuario fechar ou confirmar.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST validate direct access to fictional routes against the loaded dynamic menu.
- MUST load the menu before deciding access when state is empty.
- MUST show a fixed Bootstrap-like alert for denied access.
- MUST redirect to `/laboratorio` after user closes or confirms the alert.
- MUST NOT treat this guard as backend/API security.
</requirements>

## Subtasks
- [x] 8.1 Add route guard or equivalent validation for fictional routes.
- [x] 8.2 Add denied-access alert state/component.
- [x] 8.3 Redirect to `/laboratorio` after alert close/confirm.
- [x] 8.4 Handle menu loading and menu loading failure during direct route access.
- [x] 8.5 Add tests for allowed and denied direct routes.

## Implementation Details
Use the shared menu state from task 05 and routes from task 07. The alert can live in `App.vue`, a layout component, or a small dedicated component, but it must remain visible until the user acts.

### Relevant Files
- `frontend/src/rotas/rotas.ts` — route guard location.
- `frontend/src/App.vue` — global alert placement candidate.
- `frontend/src/composables/useMenuDinamico.ts` — source of permitted routes.
- `frontend/src/paginas/TelaFicticia.vue` — shared page from task 07.

### Dependent Files
- `frontend/src/componentes/layout/CabecalhoPrincipal.vue` — links should only show permitted routes, reducing denied alerts from normal clicks.

### Related ADRs
- [ADR-002: Menu Corporativo Didatico Com Controle De Rota](adrs/adr-002.md) — requires fixed alert and redirect.
- [ADR-005: Composable Simples E Rotas Hibridas No Frontend](adrs/adr-005.md) — guard uses shared composable state.

## Deliverables
- Direct-route access validation for fictional routes.
- Fixed denied-access alert with close/confirm behavior.
- Redirect to `/laboratorio` after denied alert action.
- Unit and route tests for allowed/denied cases.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for route guard flow **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Known route in dynamic menu is allowed.
  - [x] Unknown fictional route triggers denied alert.
  - [x] Closing denied alert redirects to `/laboratorio`.
  - [x] Confirming denied alert redirects to `/laboratorio`.
  - [x] Menu loading failure shows a controlled denied/unavailable state.
- Integration tests:
  - [x] Vue Router direct navigation to denied fictional route does not render permitted-access page.
  - [x] Vue Router direct navigation to allowed fictional route renders permitted-access page.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Direct URL access behaves exactly as the PRD describes for allowed and denied routes.
