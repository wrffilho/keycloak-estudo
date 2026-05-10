---
status: completed
title: Configurar seguranca JWT com Spring Resource Server
type: backend
complexity: high
dependencies:
  - task_01
---

# Task 4: Configurar seguranca JWT com Spring Resource Server

## Overview
Esta tarefa configura a aplicacao como Resource Server para validar tokens JWT emitidos pelo Keycloak. Ela cria a fundacao tecnica para autenticar usuarios e preparar a aplicacao para autorizacao por permissoes/scopes.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST configure Spring Security Resource Server JWT validation.
- MUST allow public routes without authentication.
- MUST require authentication for authenticated and authorized route groups.
- MUST define configuration properties for local Keycloak issuer.
- MUST return `401` for missing or invalid authentication.
- SHOULD keep Policy Enforcer out of the primary V1 enforcement path.
</requirements>

## Subtasks
- [x] 4.1 Configurar dependencias de seguranca necessarias.
- [x] 4.2 Configurar regras base para rotas publicas e protegidas.
- [x] 4.3 Configurar validacao JWT via issuer local do Keycloak.
- [x] 4.4 Criar propriedades de configuracao para ambiente local.
- [x] 4.5 Criar testes de comportamento de autenticacao.

## Implementation Details
Seguir a abordagem da TechSpec "Integration Points > Keycloak" e ADR-004. Esta tarefa nao precisa criar o realm Keycloak; ela deve preparar o backend para confiar no issuer quando o ambiente existir.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define Resource Server e fluxo JWT.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-004.md` — registra abordagem hibrida.

### Dependent Files
- `backend/src/main/java/.../configuracao` — configuracao de seguranca a criar.
- `backend/src/main/resources/application.yml` ou equivalente — propriedades de issuer a criar.
- `backend/src/test/java/.../configuracao` — testes de seguranca a criar.

### Related ADRs
- [ADR-004: Autorizacao Hibrida com Spring Resource Server e Mapeamento Keycloak](adrs/adr-004.md) — define JWT/scopes no Spring como caminho principal.

## Deliverables
- Spring Security Resource Server configurado.
- Rotas publicas liberadas e demais rotas protegidas conforme categoria.
- Propriedades de issuer local documentadas no codigo/configuracao.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for authentication behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar conversores/configuracoes auxiliares criadas nesta tarefa.
- Integration tests:
  - [x] Rota publica retorna `200` sem token.
  - [x] Rota autenticada retorna `401` sem token.
  - [x] Rota autenticada aceita usuario JWT simulado.
  - [x] Token invalido ou ausente nao acessa rotas protegidas.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Aplicacao esta pronta para validar JWT do Keycloak local.
- Policy Enforcer nao foi introduzido como mecanismo principal da V1.
