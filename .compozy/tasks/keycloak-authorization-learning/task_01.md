---
status: completed
title: Criar base Spring Boot do backend
type: backend
complexity: medium
dependencies: []
---

# Task 1: Criar base Spring Boot do backend

## Overview
Esta tarefa cria a base da aplicacao backend em Java + Spring Boot para o laboratorio. Ela estabelece o ponto de partida do monolito modular simples descrito na TechSpec, sem ainda implementar regras de negocio ou integracao completa com Keycloak.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST create a runnable Java Spring Boot backend project.
- MUST establish package boundaries for `documentos`, `seguranca`, `configuracao`, and `compartilhado` only where needed.
- MUST include a basic application startup path that can be verified by tests or build tooling.
- MUST keep naming and documentation comments in PT-BR when they help learning.
- SHOULD avoid adding frontend, persistence, or unnecessary modules in V1.
</requirements>

## Subtasks
- [x] 1.1 Criar estrutura inicial do projeto backend Spring Boot.
- [x] 1.2 Configurar build com dependencias basicas para web, seguranca e testes.
- [x] 1.3 Criar classe principal da aplicacao.
- [x] 1.4 Criar organizacao inicial de pacotes coerente com monolito modular simples.
- [x] 1.5 Adicionar teste minimo de carregamento de contexto.

## Implementation Details
Criar os arquivos base do projeto na raiz do repositorio ou em um diretorio backend, conforme a implementacao escolher ao iniciar. Referenciar a TechSpec nas secoes "System Architecture" e "Development Sequencing" para manter os limites de modulo.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — define a arquitetura backend-first e os modulos esperados.
- `.compozy/tasks/keycloak-authorization-learning/_prd.md` — define a experiencia V1 e o escopo sem frontend.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-003.md` — registra Java + Spring Boot com monolito modular simples.

### Dependent Files
- `backend/pom.xml` — arquivo de build do backend para compilar e testar o backend.
- `backend/src/main/java/...` — raiz de codigo da aplicacao a ser criada.
- `backend/src/test/java/...` — raiz de testes automatizados a ser criada.

### Related ADRs
- [ADR-003: Stack Java Spring Boot com Monolito Modular Simples](adrs/adr-003.md) — define stack e organizacao modular.

## Deliverables
- Projeto Spring Boot backend criado e executavel.
- Estrutura inicial de pacotes alinhada a monolito modular simples.
- Build configurado com dependencias de aplicacao e teste.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for application startup **(REQUIRED)**

## Tests
- Unit tests:
  - [x] Verificar que classes utilitarias ou configuracoes iniciais criadas nesta tarefa possuem comportamento esperado.
- Integration tests:
  - [x] Contexto Spring Boot carrega sem erro.
  - [x] Build do projeto executa a fase de testes com sucesso.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Backend compila em ambiente local.
- Aplicacao Spring Boot possui ponto de entrada claro.
- Estrutura inicial nao inclui frontend, banco ou recursos fora da V1.
