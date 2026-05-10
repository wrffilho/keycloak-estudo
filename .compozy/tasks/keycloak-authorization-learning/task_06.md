---
status: completed
title: Criar ambiente Docker do Keycloak
type: infra
complexity: medium
dependencies:
  - task_01
---

# Task 6: Criar ambiente Docker do Keycloak

## Overview
Esta tarefa cria o ambiente local de Keycloak via Docker Compose. Ela garante que o laboratorio possa partir de um ambiente zerado e subir o provedor de identidade necessario para os testes manuais.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST provide Docker Compose for local Keycloak.
- MUST configure development credentials suitable only for local learning.
- MUST reserve/import a path for realm configuration used by task_07.
- MUST document local ports and startup expectations.
- MUST avoid production hardening scope in V1.
</requirements>

## Subtasks
- [x] 6.1 Criar Docker Compose para Keycloak local.
- [x] 6.2 Definir credenciais locais de desenvolvimento.
- [x] 6.3 Preparar diretorio para import de realm.
- [x] 6.4 Documentar portas e comandos basicos de inicializacao.
- [x] 6.5 Criar verificacoes automatizadas possiveis para arquivos de infra.

## Implementation Details
Criar arquivos de infraestrutura na raiz ou em diretorio dedicado, mantendo o setup simples. A configuracao do realm sera entregue na `task_07`.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_prd.md` — exige ambiente local do zero.
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define Keycloak local via Docker Compose.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-001.md` — registra Docker Compose como parte da V1.

### Dependent Files
- `docker-compose.yml` ou equivalente — compose local a criar.
- `infra/keycloak` ou equivalente — diretorio para realm importado a criar.
- `README.md` — sera atualizado posteriormente pela documentacao.

### Related ADRs
- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — exige Keycloak via Docker.

## Deliverables
- Compose local do Keycloak criado.
- Diretorio de import de realm preparado.
- Portas e credenciais locais documentadas em arquivo ou comentarios adequados.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for infrastructure configuration **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Validar arquivos de configuracao com parser/linter disponivel quando houver ferramenta no projeto.
- Integration tests:
  - [x] `docker compose config` valida o compose sem erro quando Docker estiver disponivel.
  - [x] Caminho de import do realm existe e e referenciado pelo compose.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Keycloak pode ser iniciado localmente via Docker Compose.
- Ambiente nao inclui banco/app extra fora do escopo da V1.
