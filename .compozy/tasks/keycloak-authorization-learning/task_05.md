---
status: completed
title: Implementar conversao de permissoes e endpoints autorizados
type: backend
complexity: high
dependencies:
  - task_02
  - task_04
---

# Task 5: Implementar conversao de permissoes e endpoints autorizados

## Overview
Esta tarefa implementa autorizacao fina nos endpoints de `documentos`. Ela conecta permissoes/scopes vindos do token com regras de acesso da aplicacao para demonstrar `200`, `401` e `403` de forma didatica.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST expose authorized `documentos` endpoints from the TechSpec.
- MUST enforce `documentos:ler`, `documentos:criar`, `documentos:editar`, and `documentos:aprovar`.
- MUST convert JWT claims/scopes into Spring authorities consistently.
- MUST return `403` when an authenticated user lacks required permission.
- MUST keep public, authenticated, and authorized endpoint categories visibly separate.
</requirements>

## Subtasks
- [x] 5.1 Criar conversor de permissoes/scopes para authorities.
- [x] 5.2 Criar endpoint para listar permissoes percebidas pela aplicacao.
- [x] 5.3 Criar endpoints autorizados de `documentos`.
- [x] 5.4 Aplicar permissoes esperadas em cada endpoint.
- [x] 5.5 Criar testes de sucesso e acesso negado por permissao.

## Implementation Details
Seguir TechSpec "API Endpoints", "Keycloak Authorization Mapping" e "Core Interfaces". Esta tarefa deve criar o comportamento que depois sera alinhado ao realm importado na `task_07`.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define endpoints, permissoes e interfaces.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-004.md` — define conversao JWT/scopes no Spring.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-005.md` — define dados em memoria.

### Dependent Files
- `backend/src/main/java/.../documentos` — endpoints de documentos e servico do dominio.
- `backend/src/main/java/.../seguranca` — conversor de permissoes e helpers.
- `backend/src/test/java/...` — testes de autorizacao por endpoint.

### Related ADRs
- [ADR-004: Autorizacao Hibrida com Spring Resource Server e Mapeamento Keycloak](adrs/adr-004.md) — define enforcement no Spring com mapeamento Keycloak.
- [ADR-005: Dados em Memoria e Testes Essenciais para V1](adrs/adr-005.md) — define dados em memoria e testes centrais.

## Deliverables
- Endpoints de `documentos` implementados.
- Conversao de permissoes/scopes implementada.
- Regras de acesso aplicadas a cada operacao.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for authorized endpoints **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Conversor extrai `documentos:ler` de claims/scopes simulados.
  - [x] Conversor retorna conjunto vazio quando token nao possui permissoes esperadas.
  - [x] Helper de permissao identifica permissao presente e ausente.
- Integration tests:
  - [x] `GET /documentos` retorna `403` com usuario sem `documentos:ler`.
  - [x] `GET /documentos` retorna `200` com `documentos:ler`.
  - [x] `POST /documentos` retorna `403` sem `documentos:criar`.
  - [x] `PUT /documentos/{id}` retorna `200` com `documentos:editar`.
  - [x] `POST /documentos/{id}/aprovar` retorna `200` com `documentos:aprovar`.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Cada endpoint autorizado exige a permissao definida na TechSpec.
- Usuario autenticado sem permissao recebe `403`.
