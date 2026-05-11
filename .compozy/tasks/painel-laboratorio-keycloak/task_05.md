---
status: completed
title: Implementar servicos e estado de documentos
type: frontend
complexity: medium
dependencies:
  - task_04
---

# Task 05: Implementar servicos e estado de documentos

## Overview
Esta tarefa conecta o frontend aos endpoints de documentos existentes no backend. Ela cria os servicos e composables necessarios para listar, consultar detalhe, criar, editar e aprovar documentos respeitando o modelo do laboratorio.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST consumir `GET /documentos`.
- MUST consumir `GET /documentos/{id}`.
- MUST consumir `POST /documentos`.
- MUST consumir `PUT /documentos/{id}`.
- MUST consumir `POST /documentos/{id}/aprovar`.
- SHOULD reutilizar permissoes da task_04 para expor estados de acoes.
</requirements>

## Subtasks
- [ ] 5.1 Criar tipos de documentos.
- [ ] 5.2 Criar `servicoDeDocumentos.ts`.
- [ ] 5.3 Criar `useDocumentos`.
- [ ] 5.4 Integrar estados de carregamento e erro.
- [ ] 5.5 Testar chamadas, payloads e estados principais.

## Implementation Details
Seguir a TechSpec nas secoes "API Endpoints > Documentos", "Data Models" e "Data Flow". Esta tarefa nao deve montar a UI final; deve entregar a camada de dados para as rotas e componentes.

### Relevant Files
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/DocumentosController.java` — contrato dos endpoints de documentos.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/DocumentoResposta.java` — formato de resposta.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/CriarDocumentoRequisicao.java` — payload de criacao.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/documentos/EditarDocumentoRequisicao.java` — payload de edicao.
- `frontend/src/tipos/documento.ts` — tipos do dominio documentos.
- `frontend/src/servicos/servicoDeDocumentos.ts` — servico de documentos a criar.
- `frontend/src/composables/useDocumentos.ts` — composable de documentos a criar.

### Dependent Files
- `frontend/src/paginas/Documentos.vue` — consumira lista e criacao.
- `frontend/src/paginas/DocumentoDetalhe.vue` — consumira detalhe, edicao e aprovacao.
- `frontend/src/composables/useDiagnosticoDeAcao.ts` — registrara resultados das acoes.

### Related ADRs
- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — define documentos reais como nucleo didatico.
- [ADR-004: Organizacao do Frontend por Composables e Servicos de Dominio](adrs/adr-004.md) — orienta servicos por dominio.

## Deliverables
- Servico de documentos cobrindo endpoints do backend.
- Composable de documentos com estado reutilizavel.
- Tipos de documentos alinhados ao backend.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for document service behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] `listar` chama `GET /documentos`.
  - [ ] `buscarPorId` chama `GET /documentos/{id}` com id correto.
  - [ ] `criar` envia `titulo` e `conteudo` para `POST /documentos`.
  - [ ] `editar` envia `titulo` e `conteudo` para `PUT /documentos/{id}`.
  - [ ] `aprovar` chama `POST /documentos/{id}/aprovar`.
- Integration tests:
  - [ ] Composable atualiza lista apos criacao simulada.
  - [ ] Composable propaga erro `403` para camada de diagnostico futura.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Frontend possui camada de documentos completa para o MVP.
- Contratos do backend foram respeitados sem duplicar regra de negocio.
