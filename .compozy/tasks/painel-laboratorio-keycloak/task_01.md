---
status: completed
title: Preparar client SPA e CORS para o frontend
type: infra
complexity: medium
dependencies: []
---

# Task 01: Preparar client SPA e CORS para o frontend

## Overview
Esta tarefa prepara o Keycloak e o backend para que o futuro modulo `frontend/` consiga autenticar usuarios reais e chamar a API local. Ela cria a base de integracao segura entre navegador, Keycloak e backend sem expor segredo no frontend.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST adicionar um client SPA publico para o frontend no realm Keycloak.
- MUST manter o client `laboratorio-api` como client da API, sem transformá-lo em SPA.
- MUST garantir que o frontend nao dependa de client secret no navegador.
- MUST preparar o backend para aceitar chamadas vindas da origem local do frontend.
- SHOULD manter redirect URIs e web origins especificas para ambiente local.
</requirements>

## Subtasks
- [ ] 1.1 Adicionar client SPA publico ao realm do laboratorio.
- [ ] 1.2 Definir redirect URIs e web origins locais do frontend.
- [ ] 1.3 Avaliar e adicionar configuracao CORS no backend quando necessario.
- [ ] 1.4 Manter issuer e validacao JWT compatíveis com o backend atual.
- [ ] 1.5 Atualizar testes ou verificacoes de seguranca afetadas pela configuracao.

## Implementation Details
Seguir a TechSpec nas secoes "Integration Points > Keycloak", "Integration Points > Backend" e "Development Sequencing". Esta tarefa toca a infraestrutura de autenticacao e, se necessario, a configuracao de seguranca do backend.

### Relevant Files
- `infra/keycloak/laboratorio-keycloak-realm.json` — realm atual com usuarios, permissoes e client `laboratorio-api`.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/configuracao/SegurancaConfig.java` — configuracao Spring Security que pode receber CORS.
- `backend/src/main/resources/application.yml` — propriedades locais do backend.
- `backend/src/main/resources/application-docker.yml` — propriedades do backend em Docker.
- `backend/src/test/java/br/com/wanderlei/keycloakestudo/web/EndpointsWebTest.java` — testes web existentes para comportamento de seguranca.

### Dependent Files
- `docker-compose.yml` — sera ajustado depois para incluir o frontend e suas origens.
- `README.md` — sera atualizado depois com comandos e URLs.
- `frontend/` — futura origem do SPA depende desta configuracao.

### Related ADRs
- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — exige client SPA proprio e autorizacao visivel.
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — define o frontend que consumira essa configuracao.

## Deliverables
- Realm Keycloak com client SPA publico para o frontend.
- Backend preparado para chamadas do frontend local.
- Nenhum segredo de client exigido pelo navegador.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for CORS/authentication behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] Validar que configuracoes auxiliares de seguranca continuam carregando sem erro.
  - [ ] Validar que regras existentes de permissoes nao foram alteradas indevidamente.
- Integration tests:
  - [ ] `GET /publico/status` continua retornando `200` sem token.
  - [ ] `GET /usuario/perfil` continua retornando `401` sem token.
  - [ ] Requisicao com origem local do frontend recebe cabecalhos CORS esperados quando aplicavel.
  - [ ] Endpoints protegidos continuam retornando `403` quando JWT nao possui permissao.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Realm possui client SPA publico separado do `laboratorio-api`.
- Backend aceita origem local do frontend sem relaxar regras de autorizacao.
