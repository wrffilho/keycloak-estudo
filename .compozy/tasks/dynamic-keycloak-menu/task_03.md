---
status: completed
title: Integrar backend com Keycloak Admin REST API
type: backend
complexity: high
dependencies:
  - task_02
---

# Task 03: Integrar backend com Keycloak Admin REST API

## Overview
Esta tarefa cria a integracao backend com a Admin REST API do Keycloak para ler o catalogo do client `laboratorio-menu`. Ela deve isolar autenticacao administrativa, consulta de resources e consulta de roles efetivas do usuario.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST keep Keycloak admin credentials only in backend configuration.
- MUST retrieve `frontend-menu` resources from `laboratorio-menu`.
- MUST retrieve effective user roles for `laboratorio-menu`.
- MUST convert Keycloak failures to controlled backend exceptions.
- MUST NOT expose admin tokens, secrets, or raw Keycloak payloads.
</requirements>

## Subtasks
- [x] 3.1 Add backend configuration properties for Keycloak admin integration.
- [x] 3.2 Implement service to obtain an admin/service token.
- [x] 3.3 Implement lookup for the `laboratorio-menu` client.
- [x] 3.4 Implement retrieval of `frontend-menu` resources and attributes.
- [x] 3.5 Implement retrieval of effective menu roles for a username.
- [x] 3.6 Add controlled error handling for Keycloak failures.

## Implementation Details
Use existing Spring Boot patterns and keep the integration in a dedicated class/service. See TechSpec "Integration Points" for required configuration and behavior. Do not mount the final menu tree in this task.

### Relevant Files
- `backend/src/main/resources/application.yml` — add local config defaults.
- `backend/src/main/resources/application-docker.yml` — add Docker config where needed.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/configuracao/SegurancaConfig.java` — current security/resource-server setup.
- `backend/pom.xml` — confirm existing dependencies are enough before adding anything.

### Dependent Files
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/UsuarioController.java` — later endpoint consumes the integration indirectly.
- `infra/keycloak/laboratorio-keycloak-realm.json` — integration must match client and role names from task 01.

### Related ADRs
- [ADR-004: Integracao Backend Keycloak Via Admin REST API](adrs/adr-004.md) — establishes Admin REST API as the integration path.

## Deliverables
- Backend Keycloak admin integration service.
- Backend configuration properties for menu client/admin credentials.
- Controlled exception/error model for Keycloak failures.
- Unit tests with mocked HTTP/client behavior.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for configuration loading **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Successful token retrieval uses configured realm and admin client.
  - [x] Client lookup returns the UUID for `laboratorio-menu`.
  - [x] Resource retrieval filters `type = frontend-menu`.
  - [x] User role retrieval returns only roles for `laboratorio-menu`.
  - [x] HTTP 401/403/500 from Keycloak becomes controlled backend failure.
- Integration tests:
  - [x] Spring context loads with menu Keycloak properties.
  - [x] Missing required secret fails with a clear configuration error or safe disabled behavior.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Backend can read menu catalog data and user menu roles through an isolated integration boundary.
