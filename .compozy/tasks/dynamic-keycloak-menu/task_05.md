---
status: completed
title: Criar servico e composable de menu no frontend
type: frontend
complexity: medium
dependencies:
  - task_04
---

# Task 05: Criar servico e composable de menu no frontend

## Overview
Esta tarefa cria o contrato frontend para carregar e armazenar o menu dinamico. Ela segue o padrao existente de servicos HTTP e composables, sem adicionar store global.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST create `ItemDeMenu` and response types in frontend.
- MUST create a menu service that calls `GET /usuario/menu`.
- MUST create `useMenuDinamico` with loading, error and menu state.
- MUST expose helpers to find a menu item by route/id for later route checks.
- MUST follow existing composable/service naming in PT-BR.
</requirements>

## Subtasks
- [x] 5.1 Add frontend menu types.
- [x] 5.2 Add `servicoDeMenu` using existing HTTP client.
- [x] 5.3 Add `useMenuDinamico` with shared state.
- [x] 5.4 Add helper to flatten/find menu items.
- [x] 5.5 Add tests for success and failure states.

## Implementation Details
Follow the patterns in `servicoDeUsuario.ts`, `clienteHttp.ts` and `usePermissoes.ts`. Do not render UI in this task.

### Relevant Files
- `frontend/src/servicos/clienteHttp.ts` — shared authenticated request helper.
- `frontend/src/servicos/servicoDeUsuario.ts` — existing service pattern.
- `frontend/src/composables/usePermissoes.ts` — shared reactive state pattern.
- `frontend/src/tipos/seguranca.ts` — existing type organization reference.

### Dependent Files
- `frontend/src/componentes/layout/CabecalhoPrincipal.vue` — later consumes the composable.
- `frontend/src/rotas/rotas.ts` — later uses menu lookup for guard.

### Related ADRs
- [ADR-005: Composable Simples E Rotas Hibridas No Frontend](adrs/adr-005.md) — establishes composable approach.

## Deliverables
- Frontend menu type definitions.
- `servicoDeMenu` calling `/usuario/menu`.
- `useMenuDinamico` with state and lookup helpers.
- Unit tests for service/composable behavior.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for HTTP contract mocking **(REQUIRED)**

## Tests
- Unit tests:
  - [x] `servicoDeMenu` calls `/usuario/menu` and returns `itens`.
  - [x] `useMenuDinamico` stores returned menu items.
  - [x] `useMenuDinamico` sets error message when request fails.
  - [x] Lookup helper finds nested item by route.
  - [x] Lookup helper returns undefined for unknown route.
- Integration tests:
  - [x] Mocked authenticated HTTP flow returns menu without reading token claims in menu code.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Frontend has reusable menu state available for header and route validation.
