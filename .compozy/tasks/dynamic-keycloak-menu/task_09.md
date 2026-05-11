---
status: completed
title: Atualizar documentacao do fluxo de menu dinamico
type: docs
complexity: low
dependencies:
  - task_08
---

# Task 09: Atualizar documentacao do fluxo de menu dinamico

## Overview
Esta tarefa documenta como executar e entender o menu dinamico do laboratorio. A documentacao deve explicar a diferenca entre menu, rota de frontend e endpoint backend em portugues do Brasil.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST document the login-to-menu flow in PT-BR.
- MUST list demonstrable users and expected menu differences.
- MUST explain that menu visibility does not replace backend authorization.
- MUST include manual steps for allowed fictional route and denied direct route.
- SHOULD link to existing Keycloak/front-end docs where relevant.
</requirements>

## Subtasks
- [x] 9.1 Add or update documentation for dynamic menu flow.
- [x] 9.2 Document users and expected menu examples.
- [x] 9.3 Document allowed route behavior.
- [x] 9.4 Document denied direct-route behavior.
- [x] 9.5 Cross-link glossary or related security docs.

## Implementation Details
Prefer adding a focused doc under `docs/` and linking it from `README.md` if appropriate. Keep language practical and consistent with existing docs.

### Relevant Files
- `README.md` — main project entry point.
- `docs/glossario.md` — project terminology.
- `docs/hasrole-hasauthority-haspermission.md` — related authorization concepts.
- `docs/spring-resource-server-vs-policy-enforcer.md` — existing Keycloak authorization explanation.

### Dependent Files
- `.compozy/tasks/dynamic-keycloak-menu/_prd.md` — source of product behavior.
- `.compozy/tasks/dynamic-keycloak-menu/_techspec.md` — source of technical boundaries.

### Related ADRs
- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — explains client separation.
- [ADR-002: Menu Corporativo Didatico Com Controle De Rota](adrs/adr-002.md) — explains route and alert behavior.

## Deliverables
- PT-BR documentation for dynamic menu.
- User/menu expectation table.
- Manual validation steps.
- Documentation links from README or nearby docs where useful.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for documentation examples **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Documentation includes the phrase explaining menu is not backend security.
  - [x] Documentation lists every demonstrable user introduced by the feature.
  - [x] Documentation includes allowed and denied route examples.
- Integration tests:
  - [x] Manual validation steps run against local environment after implementation.
  - [x] Links from README/docs resolve to existing files.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- A developer can run the local lab and understand the dynamic menu behavior from the docs.
