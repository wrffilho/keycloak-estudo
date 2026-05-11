---
status: completed
title: Configurar catalogo de menu no Keycloak
type: infra
complexity: medium
dependencies: []
---

# Task 01: Configurar catalogo de menu no Keycloak

## Overview
Esta tarefa adiciona ao realm o client confidencial `laboratorio-menu`, roles de visibilidade, usuarios ficticios e resources de menu/submenu. Ela cria a massa inicial que permite demonstrar menus corporativos diferentes por usuario.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST add a confidential `laboratorio-menu` client to the Keycloak realm.
- MUST define client roles for menu and submenu visibility.
- MUST add resources with `type = frontend-menu` and required attributes from the TechSpec.
- MUST add 1 or 2 fictional users with `laboratorio-menu` roles.
- SHOULD keep all labels and examples in PT-BR.
</requirements>

## Subtasks
- [x] 1.1 Add `laboratorio-menu` to the realm import.
- [x] 1.2 Define representative roles for Cadastros, Operacoes, Relatorios and Administracao.
- [x] 1.3 Add resources/attributes for at least 3 root menus and 6 submenus.
- [x] 1.4 Assign menu roles to existing users and fictional users.
- [x] 1.5 Ensure the realm still imports with existing frontend/API clients.

## Implementation Details
Modify the Keycloak realm JSON under `infra/keycloak/`. Follow TechSpec sections "Data Models" and "Integration Points" for required resource attributes. Keep this task limited to realm data and any supporting config needed for local import.

### Relevant Files
- `infra/keycloak/laboratorio-keycloak-realm.json` — realm import containing users, clients, roles and authorization resources.
- `docker-compose.yml` — imports the realm into the local Keycloak container.

### Dependent Files
- `backend/src/main/resources/application.yml` — later tasks need menu client identifiers to match realm config.
- `.compozy/tasks/dynamic-keycloak-menu/_techspec.md` — defines expected client/resource conventions.

### Related ADRs
- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — establishes the dedicated menu client.
- [ADR-003: Catalogo Hibrido Com Resources E Roles No Client De Menu](adrs/adr-003.md) — defines resources plus roles as the menu model.

## Deliverables
- Updated Keycloak realm with `laboratorio-menu`.
- Initial menu resources and role assignments.
- Manual realm import check documented in task notes or commit message.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for realm import assumptions **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validate JSON structure with existing formatter/parser used by the project or `jq` if available.
  - [x] Confirm every `frontend-menu` resource has `id`, `rotulo`, `rota`, `tipo`, `ordem` and `roleNecessaria`.
  - [x] Confirm each `roleNecessaria` exists as a client role in `laboratorio-menu`.
- Integration tests:
  - [x] Start local Keycloak with the updated realm and confirm the realm imports without errors.
  - [x] Confirm existing users `leitor`, `editor` and `aprovador` still exist after import.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Realm contains `laboratorio-menu` with menu resources and roles.
- At least 4 demonstrable users have different menu role combinations.
