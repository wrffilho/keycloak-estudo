---
status: completed
title: Implementar dominio `documentos` em memoria
type: backend
complexity: medium
dependencies:
  - task_01
---

# Task 2: Implementar dominio `documentos` em memoria

## Overview
Esta tarefa implementa o dominio didatico `documentos` com dados em memoria. O objetivo e fornecer um conjunto previsivel de operacoes para demonstrar autorizacao sem adicionar persistencia real ou complexidade de negocio desnecessaria.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST create the document domain model described in the TechSpec.
- MUST provide in-memory operations to list, fetch, create, edit, and approve documents.
- MUST return predictable sample data suitable for Postman and documentation.
- MUST handle missing documents and invalid document input in a clear way.
- SHOULD keep domain names in PT-BR where they improve readability.
</requirements>

## Subtasks
- [x] 2.1 Criar modelo principal de documento e seus status.
- [x] 2.2 Criar contratos de requisicao e resposta do dominio.
- [x] 2.3 Criar servico de documentos com armazenamento em memoria.
- [x] 2.4 Popular dados iniciais previsiveis para leitura, edicao e aprovacao.
- [x] 2.5 Criar testes unitarios do dominio.

## Implementation Details
Implementar o modulo `documentos` conforme TechSpec "Implementation Design". Esta tarefa nao deve criar endpoints protegidos ainda; ela entrega a base de dominio que tarefas posteriores irao expor por HTTP.

### Relevant Files
- `.compozy/tasks/keycloak-authorization-learning/_techspec.md` — descreve `ServicoDeDocumentos`, modelos e dados em memoria.
- `.compozy/tasks/keycloak-authorization-learning/adrs/adr-005.md` — registra decisao por dados em memoria.

### Dependent Files
- `backend/src/main/java/.../documentos` — pacote de dominio a ser criado.
- `backend/src/test/java/.../documentos` — testes unitarios do dominio a serem criados.

### Related ADRs
- [ADR-005: Dados em Memoria e Testes Essenciais para V1](adrs/adr-005.md) — define dados em memoria e foco nos cenarios essenciais.
- [ADR-003: Stack Java Spring Boot com Monolito Modular Simples](adrs/adr-003.md) — orienta os limites do modulo.

## Deliverables
- Dominio `documentos` implementado em memoria.
- Operacoes de listar, buscar, criar, editar e aprovar disponiveis para uso pelos endpoints.
- Dados iniciais previsiveis para roteiro Postman.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for domain wiring **(REQUIRED)**

## Tests
- Unit tests:
  - [x] `listar` retorna documentos iniciais esperados.
  - [x] `buscarPorId` retorna documento existente.
  - [x] `buscarPorId` sinaliza documento inexistente.
  - [x] `criar` adiciona documento com status inicial esperado.
  - [x] `editar` altera titulo e conteudo de documento existente.
  - [x] `aprovar` altera status para aprovado.
- Integration tests:
  - [x] Bean/servico de documentos fica disponivel no contexto da aplicacao.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Dominio funciona sem banco de dados.
- Dados de exemplo sao estaveis para documentacao e Postman.
