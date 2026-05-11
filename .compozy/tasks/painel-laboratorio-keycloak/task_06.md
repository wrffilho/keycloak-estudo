---
status: completed
title: Implementar diagnostico de acoes e feedback de autorizacao
type: frontend
complexity: medium
dependencies:
  - task_05
---

# Task 06: Implementar diagnostico de acoes e feedback de autorizacao

## Overview
Esta tarefa cria o mecanismo didatico que mostra a ultima acao relevante, o endpoint, o status e a mensagem de autorizacao. Ela transforma chamadas tecnicas em feedback compreensivel em PT-BR, mantendo o backend como fonte real de autorizacao.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST criar tipo de diagnostico de acao.
- MUST criar `useDiagnosticoDeAcao`.
- MUST registrar acao, metodo, endpoint, status e mensagem didatica.
- MUST tratar `401` e `403` com explicacoes em PT-BR.
- MUST permitir que a UI mostre acoes bloqueadas antes do clique.
- SHOULD evitar expor token bruto no diagnostico.
</requirements>

## Subtasks
- [ ] 6.1 Criar tipos de diagnostico.
- [ ] 6.2 Criar `useDiagnosticoDeAcao`.
- [ ] 6.3 Mapear mensagens didaticas para sucesso, `401`, `403` e erros inesperados.
- [ ] 6.4 Integrar diagnostico aos composables de documentos.
- [ ] 6.5 Testar estados de sucesso, bloqueio e erro.

## Implementation Details
Seguir a TechSpec nas secoes "Core Interfaces", "Monitoring and Observability" e "Technical Considerations". Esta tarefa deve entregar dados para componentes visuais, sem decidir layout final.

### Relevant Files
- `frontend/src/tipos/diagnostico.ts` — tipos do diagnostico a criar.
- `frontend/src/composables/useDiagnosticoDeAcao.ts` — composable de diagnostico a criar.
- `frontend/src/composables/useDocumentos.ts` — integracao com acoes de documentos.
- `frontend/src/composables/usePermissoes.ts` — origem para mensagens de permissao ausente.
- `backend/src/test/java/br/com/wanderlei/keycloakestudo/web/EndpointsWebTest.java` — referencia de status `401` e `403` esperados.

### Dependent Files
- `frontend/src/componentes/laboratorio/*` — exibira diagnostico.
- `frontend/src/paginas/PainelDoLaboratorio.vue` — exibira feedback da ultima acao.
- `frontend/src/paginas/Documentos.vue` — registrara acoes da lista.
- `frontend/src/paginas/DocumentoDetalhe.vue` — registrara acoes de detalhe, edicao e aprovacao.

### Related ADRs
- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — exige diagnostico explicito.
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — define foco em permissoes e respostas.

## Deliverables
- Composable de diagnostico de acao.
- Mensagens didaticas para sucesso, `401`, `403` e erro inesperado.
- Integracao basica com acoes de documentos.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for authorization feedback behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] Diagnostico registra acao permitida com status `200`.
  - [ ] Diagnostico registra criacao com status `201`.
  - [ ] Diagnostico traduz `401` como ausencia ou expiracao de autenticacao.
  - [ ] Diagnostico traduz `403` como permissao insuficiente.
  - [ ] Diagnostico nao inclui token bruto na mensagem.
- Integration tests:
  - [ ] Acao de documento atualiza diagnostico apos sucesso.
  - [ ] Erro de permissao vindo do servico atualiza diagnostico com mensagem didatica.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- UI futura consegue explicar a ultima acao sem depender de Postman.
- `401` e `403` possuem mensagens claras em PT-BR.
