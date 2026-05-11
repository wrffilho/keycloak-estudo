---
status: completed
title: Montar e expor menu do usuario no backend
type: backend
complexity: medium
dependencies:
  - task_03
---

# Task 04: Montar e expor menu do usuario no backend

## Overview
Esta tarefa monta a arvore final de menu para o usuario autenticado e expoe `GET /usuario/menu`. Ela conecta o contrato, a integracao Keycloak e o controller de usuario.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST expose `GET /usuario/menu` for authenticated users.
- MUST filter catalog items by effective `laboratorio-menu` roles.
- MUST build parent/child menu tree ordered by `ordem`.
- MUST return a clean JSON contract with `itens`.
- MUST return controlled error when Keycloak catalog cannot be read.
</requirements>

## Subtasks
- [x] 4.1 Implement menu assembly and role filtering.
- [x] 4.2 Sort root menus and children by order.
- [x] 4.3 Add endpoint under `/usuario`.
- [x] 4.4 Map Keycloak integration failures to an appropriate HTTP error.
- [x] 4.5 Ensure unauthenticated requests remain protected.

## Implementation Details
Extend the backend near `UsuarioController` or a cohesive `menu` package. Use the models from task 02 and the integration boundary from task 03. See TechSpec "API Endpoints" for response shape.

### Relevant Files
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/UsuarioController.java` — existing authenticated user endpoints.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/TratadorDeErros.java` — existing error handling pattern.
- `backend/src/test/java/br/com/wanderlei/keycloakestudo/web/EndpointsWebTest.java` — web security test pattern.

### Dependent Files
- `frontend/src/servicos/servicoDeUsuario.ts` — frontend later adds a menu service using this endpoint pattern.

### Related ADRs
- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — endpoint returns menu from Keycloak-backed catalog.
- [ADR-004: Integracao Backend Keycloak Via Admin REST API](adrs/adr-004.md) — endpoint depends on backend Keycloak integration.

## Deliverables
- `GET /usuario/menu` endpoint.
- Service that filters and assembles the menu tree.
- Backend unit/web tests for success, filtering and failure.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for authenticated endpoint access **(REQUIRED)**

## Tests
- Unit tests:
  - [x] User with role `menu:relatorios` receives Relatorios.
  - [x] User without submenu role does not receive that submenu.
  - [x] Children are nested under their parent by `pai`.
  - [x] Root and child items are sorted by `ordem`.
  - [x] Invalid catalog items are ignored or reported according to service policy.
- Integration tests:
  - [x] `GET /usuario/menu` without token returns 401.
  - [x] `GET /usuario/menu` with JWT returns 200 and `itens`.
  - [x] Keycloak integration failure returns controlled error status.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Authenticated backend endpoint returns only menus permitted for the user.
