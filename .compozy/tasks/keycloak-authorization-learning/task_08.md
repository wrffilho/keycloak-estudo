---
status: completed
title: Criar colecao Postman guiada e desafios
type: docs
complexity: medium
dependencies:
  - task_05
  - task_07
---

# Task 8: Criar colecao Postman guiada e desafios

## Overview
Esta tarefa cria a colecao Postman que conduz o aprendizado manual do laboratorio. Ela deve permitir obter tokens, executar endpoints em ordem e comparar resultados `200`, `401` e `403`.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST provide a Postman collection with guided requests.
- MUST include token acquisition for `leitor`, `editor`, and `aprovador`.
- MUST include public, authenticated, authorized, and denied scenarios.
- MUST include small challenges after the guided path.
- MUST use environment variables to reduce manual token copying.
</requirements>

## Subtasks
- [x] 8.1 Criar ambiente Postman com variaveis locais.
- [x] 8.2 Criar requests de obtencao de token para cada usuario/perfil.
- [x] 8.3 Criar roteiro guiado dos endpoints publicos, autenticados e autorizados.
- [x] 8.4 Criar requests de cenarios negativos esperados.
- [x] 8.5 Criar desafios pequenos de previsao ou alteracao de permissao.
- [x] 8.6 Validar sintaxe/importacao da colecao quando possivel.

## Implementation Details
Seguir a sequencia de "User Experience" do PRD e endpoints da TechSpec. A colecao deve ser material de aprendizado, nao apenas uma lista de requests.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_prd.md` — define roteiro guiado e desafios.
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define endpoints e usuarios/perfis.

### Dependent Files
- `postman/*.json` ou equivalente — colecao e ambiente a criar.
- `README.md` — deve apontar para importacao e uso da colecao em tarefa posterior.

### Related ADRs
- [ADR-002: Abordagem de Produto da V1 como Laboratorio Guiado e Enxuto](adrs/adr-002.md) — define Postman hibrido.

## Deliverables
- Colecao Postman com roteiro guiado.
- Ambiente Postman com variaveis necessarias.
- Requests para tokens dos usuarios de exemplo.
- Desafios práticos curtos.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for Postman artifacts **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar JSON da colecao e do ambiente.
  - [x] Verificar que a colecao contem requests para `200`, `401` e `403`.
- Integration tests:
  - [x] Importar a colecao no Postman ou validar via ferramenta compativel quando disponivel.
  - [x] Executar roteiro principal contra ambiente local quando backend e Keycloak estiverem disponiveis.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Colecao cobre todos os endpoints principais.
- Usuario consegue seguir roteiro guiado antes dos desafios.
