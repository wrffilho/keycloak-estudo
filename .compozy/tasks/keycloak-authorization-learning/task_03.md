---
status: completed
title: Criar endpoints publicos e autenticados
type: backend
complexity: medium
dependencies:
  - task_01
---

# Task 3: Criar endpoints publicos e autenticados

## Overview
Esta tarefa cria os primeiros endpoints didaticos do laboratorio: rotas publicas e rotas que exigem autenticacao. Ela estabelece a diferenca entre acesso livre e acesso com token antes da autorizacao fina por documentos.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST expose `GET /publico/status` without authentication.
- MUST expose `GET /publico/sobre` without authentication.
- MUST expose authenticated user endpoints from the TechSpec.
- MUST preserve expected `200` and `401` behavior for public and authenticated categories.
- SHOULD return didactic PT-BR response bodies where useful.
</requirements>

## Subtasks
- [x] 3.1 Criar endpoints publicos de status e sobre.
- [x] 3.2 Criar endpoints autenticados de perfil e permissoes percebidas.
- [x] 3.3 Garantir que respostas ajudem no aprendizado do laboratorio.
- [x] 3.4 Criar testes dos endpoints publicos.
- [x] 3.5 Criar testes dos endpoints autenticados com e sem usuario simulado.

## Implementation Details
Implementar os endpoints descritos na TechSpec "API Endpoints". A configuracao de seguranca completa sera refinada em `task_04`, mas esta tarefa deve deixar claro quais rotas sao publicas e quais exigem usuario autenticado.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — lista endpoints publicos e autenticados.
- `.compozy/tasks/keycloak-authorization-learning/_prd.md` — descreve a jornada Postman com `200` e `401`.

### Dependent Files
- `backend/src/main/java/.../publico` ou pacote equivalente — endpoints publicos a criar.
- `backend/src/main/java/.../seguranca` ou pacote equivalente — endpoints do usuario autenticado a criar.
- `backend/src/test/java/...` — testes MVC ou integracao a criar.

### Related ADRs
- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — separa endpoints publicos, autenticados e autorizados.
- [ADR-003: Stack Java Spring Boot com Monolito Modular Simples](adrs/adr-003.md) — orienta stack e modularidade.

## Deliverables
- Endpoints publicos disponiveis sem token.
- Endpoints autenticados disponiveis apenas com usuario/token.
- Respostas em PT-BR adequadas para aprendizado.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for public and authenticated endpoints **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar montagem das respostas dos endpoints publicos.
  - [x] Validar extracao de dados do usuario autenticado quando houver principal simulado.
- Integration tests:
  - [x] `GET /publico/status` retorna `200` sem token.
  - [x] `GET /publico/sobre` retorna `200` sem token.
  - [x] `GET /usuario/perfil` retorna `401` sem token.
  - [x] `GET /usuario/perfil` retorna `200` com usuario simulado.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Rotas publicas e autenticadas demonstram diferenca entre acesso livre e login obrigatorio.
