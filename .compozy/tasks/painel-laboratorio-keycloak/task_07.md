---
status: completed
title: Construir rotas e telas do Painel do Laboratorio
type: frontend
complexity: high
dependencies:
  - task_06
---

# Task 07: Construir rotas e telas do Painel do Laboratorio

## Overview
Esta tarefa monta a experiencia visual principal do laboratorio usando as rotas previstas na TechSpec. Ela integra sessao, permissoes, documentos, acoes bloqueadas e diagnostico em uma UI apresentavel e didatica em PT-BR.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST criar rota `/laboratorio` com painel principal.
- MUST criar rota `/documentos` com lista e acoes disponiveis.
- MUST criar rota `/documentos/:id` com detalhe e acoes de edicao/aprovacao.
- MUST exibir usuario autenticado e permissoes efetivas.
- MUST desabilitar acoes sem permissao antes do clique com explicacao clara.
- MUST exibir diagnostico da ultima acao relevante.
- SHOULD manter a tela limpa, apresentavel e sem excesso de texto.
</requirements>

## Subtasks
- [ ] 7.1 Criar layout base do laboratorio.
- [ ] 7.2 Criar painel de usuario e permissoes.
- [ ] 7.3 Criar lista de documentos e entrada para criacao.
- [ ] 7.4 Criar detalhe de documento com edicao e aprovacao.
- [ ] 7.5 Criar componente de diagnostico da ultima acao.
- [ ] 7.6 Integrar acoes bloqueadas com explicacao de permissao faltante.
- [ ] 7.7 Testar renderizacao, rotas e estados de permissao.

## Implementation Details
Seguir a TechSpec nas secoes "Frontend Routes", "Component Overview", "Data Flow" e "Testing Approach". Esta tarefa consome composables e servicos ja criados, sem alterar contratos do backend.

### Relevant Files
- `frontend/src/rotas/rotas.ts` — rotas do laboratorio.
- `frontend/src/paginas/PainelDoLaboratorio.vue` — painel principal.
- `frontend/src/paginas/Documentos.vue` — lista de documentos.
- `frontend/src/paginas/DocumentoDetalhe.vue` — detalhe de documento.
- `frontend/src/componentes/documentos/` — componentes de documentos.
- `frontend/src/componentes/laboratorio/` — painel de permissoes e diagnostico.
- `frontend/src/componentes/layout/` — estrutura visual compartilhada.

### Dependent Files
- `frontend/src/composables/useSessao.ts` — estado de usuario.
- `frontend/src/composables/usePermissoes.ts` — regras visuais de permissao.
- `frontend/src/composables/useDocumentos.ts` — dados e acoes de documentos.
- `frontend/src/composables/useDiagnosticoDeAcao.ts` — feedback da ultima acao.
- `README.md` — recebera instrucoes finais em tarefa posterior.

### Related ADRs
- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — orienta escopo visual.
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — define modo contextual e uso livre.
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — define stack e rotas.
- [ADR-004: Organizacao do Frontend por Composables e Servicos de Dominio](adrs/adr-004.md) — define fronteiras internas.

## Deliverables
- Rotas `/laboratorio`, `/documentos` e `/documentos/:id` funcionais.
- UI principal com usuario, permissoes, documentos e diagnostico.
- Acoes sem permissao desabilitadas com explicacao.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for route and permission UI behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] Painel renderiza usuario autenticado e permissoes.
  - [ ] Lista renderiza documentos recebidos pelo composable.
  - [ ] Botao de criar fica desabilitado sem `documentos:criar`.
  - [ ] Botao de editar fica desabilitado sem `documentos:editar`.
  - [ ] Botao de aprovar fica desabilitado sem `documentos:aprovar`.
- Integration tests:
  - [ ] Navegacao de `/laboratorio` para `/documentos` funciona.
  - [ ] Navegacao de `/documentos` para `/documentos/:id` preserva o contexto.
  - [ ] Diagnostico aparece apos uma acao de documento simulada.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Usuario consegue operar documentos por rotas Vue Router.
- A tela comunica permissoes e bloqueios sem depender de Postman.
