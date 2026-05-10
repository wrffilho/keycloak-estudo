---
status: completed
title: Criar realm importado com usuarios, client e permissoes
type: infra
complexity: high
dependencies:
  - task_05
  - task_06
---

# Task 7: Criar realm importado com usuarios, client e permissoes

## Overview
Esta tarefa cria a configuracao importavel do Keycloak para o laboratorio. Ela alinha usuarios, client, recursos, escopos, politicas e permissoes aos endpoints implementados no backend.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST create an importable Keycloak realm for the lab.
- MUST include users/profiles `leitor`, `editor`, and `aprovador`.
- MUST configure client data needed by the backend and Postman.
- MUST model resources, scopes, policies, and permissions for `documentos`.
- MUST align permissions with backend authorities from task_05.
- SHOULD keep names didactic and PT-BR-friendly.
</requirements>

## Subtasks
- [x] 7.1 Criar realm importavel para o laboratorio.
- [x] 7.2 Criar client usado pelo backend/Postman.
- [x] 7.3 Criar usuarios ou perfis `leitor`, `editor` e `aprovador`.
- [x] 7.4 Criar recursos, escopos, politicas e permissoes de `documentos`.
- [x] 7.5 Garantir que permissoes aparecam no token no formato esperado pelo backend.
- [x] 7.6 Criar validacoes automatizadas possiveis para o arquivo de realm.

## Implementation Details
Seguir o mapa "Keycloak Authorization Mapping" da TechSpec. Esta tarefa deve manter o realm pequeno para facilitar leitura e manutencao.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define usuarios/perfis e mapeamento de permissoes.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-004.md` — define mapeamento Keycloak com enforcement no Spring.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-002.md` — exige import automatico.

### Dependent Files
- `infra/keycloak/realm-*.json` ou equivalente — realm importavel a criar.
- `docker-compose.yml` — deve apontar para o import do realm.
- `backend/src/main/resources/application.yml` — deve estar alinhado ao issuer/client se necessario.

### Related ADRs
- [ADR-002: Abordagem de Produto da V1 como Laboratorio Guiado e Enxuto](adrs/adr-002.md) — define import automatico.
- [ADR-004: Autorizacao Hibrida com Spring Resource Server e Mapeamento Keycloak](adrs/adr-004.md) — define como permissoes aparecem para o backend.

## Deliverables
- Realm Keycloak importavel criado.
- Usuarios/perfis `leitor`, `editor` e `aprovador` configurados.
- Recursos, escopos, politicas e permissoes de `documentos` configurados.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for realm import/configuration **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar JSON/YAML do realm com parser ou ferramenta disponivel.
  - [x] Verificar que permissoes esperadas existem no arquivo de realm.
- Integration tests:
  - [x] Keycloak inicia com realm importado quando Docker estiver disponivel.
  - [x] Usuarios de exemplo conseguem obter token via fluxo documentado.
  - [x] Token contem informacao necessaria para o backend mapear permissoes.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Realm importa automaticamente no Keycloak local.
- Permissoes do realm correspondem aos endpoints protegidos.
