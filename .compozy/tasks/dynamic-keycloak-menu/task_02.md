---
status: completed
title: Criar modelos e contrato backend de menu
type: backend
complexity: low
dependencies:
  - task_01
---

# Task 02: Criar modelos e contrato backend de menu

## Overview
Esta tarefa cria os tipos backend que representam item de menu, resposta da API e dados internos extraidos do Keycloak. Ela estabelece um contrato pequeno para as tarefas de integracao e endpoint.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST create PT-BR named menu response records/classes.
- MUST represent children recursively for menu/submenu trees.
- MUST represent required attributes from Keycloak resources.
- MUST avoid exposing raw Keycloak payloads to controllers.
- SHOULD keep classes under a cohesive backend package.
</requirements>

## Subtasks
- [x] 2.1 Create response model for menu tree.
- [x] 2.2 Create internal model for raw menu catalog items.
- [x] 2.3 Define service contract for listing menu by authenticated user.
- [x] 2.4 Add validation helpers for required menu attributes.

## Implementation Details
Create backend types near the existing `seguranca` or a new cohesive package such as `menu`, following TechSpec "Core Interfaces". Do not implement Keycloak calls in this task.

### Relevant Files
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/UsuarioController.java` — future endpoint will be added near user/session behavior.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/LeitorDePermissoes.java` — example of small service contract.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/DocumentoResposta.java` — existing record style for API responses.

### Dependent Files
- `backend/src/test/java/br/com/wanderlei/keycloakestudo/web/EndpointsWebTest.java` — later endpoint tests will use the response contract.

### Related ADRs
- [ADR-003: Catalogo Hibrido Com Resources E Roles No Client De Menu](adrs/adr-003.md) — defines model inputs and visibility role.

## Deliverables
- Backend menu model classes/records.
- Backend service interface for menu listing.
- Unit tests for model validation/tree fields.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for backend contract compatibility **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Valid catalog item with all required attributes is accepted.
  - [x] Catalog item without `id` is rejected or marked invalid.
  - [x] Catalog item without `roleNecessaria` is rejected or marked invalid.
  - [x] Response model supports nested children.
- Integration tests:
  - [x] Compile backend tests with the new models and no endpoint changes.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Backend has a stable menu contract ready for integration and endpoint tasks.
