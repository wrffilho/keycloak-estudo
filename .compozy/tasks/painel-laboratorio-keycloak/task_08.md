---
status: completed
title: Integrar frontend ao Docker Compose e README
type: infra
complexity: medium
dependencies:
  - task_07
---

# Task 08: Integrar frontend ao Docker Compose e README

## Overview
Esta tarefa torna o frontend parte da experiencia operacional do laboratorio. Ela adiciona o servico `frontend` ao Docker Compose e documenta os comandos para subir tudo, subir servicos isolados e rodar o frontend em modo de desenvolvimento.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST adicionar Dockerfile ou configuracao equivalente para empacotar o frontend.
- MUST adicionar servico `frontend` ao `docker-compose.yml`.
- MUST manter `docker compose up -d --build` como caminho para subir tudo.
- MUST documentar caminho separado para desenvolvimento do frontend.
- MUST documentar URLs de Keycloak, backend e frontend.
- SHOULD documentar como subir ou parar servicos isolados.
</requirements>

## Subtasks
- [ ] 8.1 Criar empacotamento Docker do frontend.
- [ ] 8.2 Adicionar servico `frontend` ao Compose.
- [ ] 8.3 Configurar variaveis de ambiente necessarias para backend e Keycloak.
- [ ] 8.4 Atualizar README com comandos completos e comandos de desenvolvimento.
- [ ] 8.5 Validar subida local dos servicos documentados.

## Implementation Details
Seguir a TechSpec nas secoes "Integration Points > Docker Compose" e "Technical Dependencies". Esta tarefa deve preservar comandos ja adicionados para `backend` e `keycloak`.

### Relevant Files
- `docker-compose.yml` — servicos atuais `keycloak` e `backend`.
- `backend/Dockerfile` — referencia de empacotamento Docker existente.
- `README.md` — documentacao operacional do laboratorio.
- `frontend/package.json` — scripts do frontend.
- `frontend/Dockerfile` — arquivo a criar ou ajustar.

### Dependent Files
- `infra/keycloak/laboratorio-keycloak-realm.json` — redirect URI do frontend precisa combinar com URL documentada.
- `backend/src/main/resources/application-docker.yml` — configuracao Docker do backend.
- `frontend/` — modulo criado nas tarefas anteriores.

### Related ADRs
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — exige Docker completo e caminho de desenvolvimento.
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — define stack a empacotar.

## Deliverables
- Servico `frontend` no Docker Compose.
- Frontend empacotavel em Docker.
- README atualizado com comandos de execucao completa e desenvolvimento.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for Docker compose startup behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] Validar scripts do frontend usados pelo Docker.
  - [ ] Validar que variaveis de ambiente obrigatorias possuem valores documentados.
- Integration tests:
  - [ ] `docker compose config` reconhece `frontend`, `backend` e `keycloak`.
  - [ ] `docker compose build frontend` conclui sem erro.
  - [ ] README aponta URLs corretas para Keycloak, backend e frontend.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- `docker compose up -d --build` representa a experiencia completa.
- Desenvolvedor consegue rodar somente o frontend em modo dev seguindo README.
