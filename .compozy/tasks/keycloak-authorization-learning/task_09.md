---
status: completed
title: Criar documentacao PT-BR do laboratorio
type: docs
complexity: medium
dependencies:
  - task_06
  - task_07
  - task_08
---

# Task 9: Criar documentacao PT-BR do laboratorio

## Overview
Esta tarefa cria a documentacao principal em portugues do Brasil para rodar e entender o laboratorio. Ela deve explicar o caminho entre endpoint, token, permissao e resultado HTTP sem linguagem desnecessariamente academica.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST provide a README in PT-BR for setup and learning flow.
- MUST explain public, authenticated, authorized, and denied endpoint categories.
- MUST document the map URI -> recurso -> escopo -> politica -> permissao.
- MUST document Postman import and execution order.
- MUST include a glossary for core Keycloak concepts.
- SHOULD include troubleshooting for common local setup issues.
</requirements>

## Subtasks
- [x] 9.1 Criar README principal com setup do zero.
- [x] 9.2 Documentar como subir Keycloak e backend.
- [x] 9.3 Documentar uso da colecao Postman.
- [x] 9.4 Criar mapa de endpoints e permissoes.
- [x] 9.5 Criar glossario PT-BR dos conceitos do Keycloak.
- [x] 9.6 Criar secao curta comparando Spring Resource Server e Policy Enforcer.

## Implementation Details
Referenciar PRD e TechSpec sem duplicar detalhes demais. A documentacao deve ser suficiente para um usuario com ambiente zerado rodar o laboratorio e entender os resultados.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_prd.md` — define a experiencia e sucesso do usuario.
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define endpoints, ambiente e mapeamentos.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-004.md` — explica abordagem hibrida.

### Dependent Files
- `README.md` — documentacao principal a criar.
- `docs/` — documentacao complementar a criar se necessario.
- `postman/` — artefatos a referenciar.
- `infra/keycloak/` — realm e compose a referenciar.

### Related ADRs
- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — define foco PT-BR e Postman.
- [ADR-004: Autorizacao Hibrida com Spring Resource Server e Mapeamento Keycloak](adrs/adr-004.md) — exige comparacao clara com Policy Enforcer.

## Deliverables
- README PT-BR completo para V1.
- Glossario de conceitos do Keycloak.
- Mapa endpoint/permissao documentado.
- Secao de troubleshooting local.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for documentation accuracy **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar links internos da documentacao quando ferramenta estiver disponivel.
  - [x] Verificar que README menciona todos os endpoints principais.
- Integration tests:
  - [x] Seguir comandos documentados em ambiente local limpo quando possivel.
  - [x] Confirmar que nomes de arquivos citados no README existem.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- README permite rodar o laboratorio do zero.
- Documentacao explica por que cada cenario retorna `200`, `401` ou `403`.
