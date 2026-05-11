---
status: completed
title: Renderizar menu dinamico no cabecalho
type: frontend
complexity: medium
dependencies:
  - task_05
---

# Task 06: Renderizar menu dinamico no cabecalho

## Overview
Esta tarefa substitui a navegacao fixa do cabecalho por menus carregados do backend. O cabecalho deve lidar com estados de carregamento, erro e menu vazio sem quebrar a pagina.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST render root menus and submenus from `useMenuDinamico`.
- MUST remove hardcoded `Laboratorio`/`Documentos` navigation links only after dynamic equivalents exist.
- MUST show a clear loading or unavailable state.
- MUST keep responsive header behavior.
- SHOULD keep visual style consistent with existing CSS.
</requirements>

## Subtasks
- [x] 6.1 Load menu for authenticated users in the header flow.
- [x] 6.2 Render root items and child items.
- [x] 6.3 Preserve brand/home navigation.
- [x] 6.4 Add UI state for loading, empty and error cases.
- [x] 6.5 Add component tests for rendered menu states.

## Implementation Details
Modify `CabecalhoPrincipal.vue` and use the composable from task 05. Keep the component focused on rendering and events; no Keycloak-specific logic belongs here.

### Relevant Files
- `frontend/src/componentes/layout/CabecalhoPrincipal.vue` — current fixed header.
- `frontend/src/App.vue` — header placement.
- `frontend/src/estilos.css` — global visual tokens/styles.

### Dependent Files
- `frontend/src/rotas/rotas.ts` — route links must match registered routes.
- `frontend/src/composables/useSessao.ts` — header may need authentication state.

### Related ADRs
- [ADR-005: Composable Simples E Rotas Hibridas No Frontend](adrs/adr-005.md) — header consumes shared composable state.

## Deliverables
- Dynamic header navigation.
- Loading/error/empty states for menu.
- Component tests for menu rendering.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for header plus composable mocked state **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Header renders root menu item returned by composable.
  - [x] Header renders submenu item returned by composable.
  - [x] Header shows error state when menu loading fails.
  - [x] Header preserves brand link to `/laboratorio`.
- Integration tests:
  - [x] Header navigation links point to routes from the dynamic menu contract.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Header no longer depends on hardcoded navigation for dynamic menu items.
