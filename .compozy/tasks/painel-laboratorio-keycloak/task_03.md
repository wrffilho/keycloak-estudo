---
status: completed
title: Implementar autenticacao Keycloak no frontend
type: frontend
complexity: medium
dependencies:
  - task_02
---

# Task 03: Implementar autenticacao Keycloak no frontend

## Overview
Esta tarefa adiciona login e logout reais via Keycloak no frontend. Ela cria a camada de sessao que as tarefas seguintes usarao para carregar usuario, permissoes e chamar endpoints protegidos.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST implementar autenticacao real usando o client SPA publico do Keycloak.
- MUST expor acoes de login e logout para a UI.
- MUST manter token fora de logs e fora do foco visual da aplicacao.
- MUST fornecer Bearer token para servicos de dominio que chamarem o backend.
- SHOULD tratar expiracao ou ausencia de sessao com mensagem didatica.
</requirements>

## Subtasks
- [ ] 3.1 Criar `servicoDeAutenticacao.ts`.
- [ ] 3.2 Criar `useSessao`.
- [ ] 3.3 Integrar inicializacao da autenticacao ao bootstrap do frontend.
- [ ] 3.4 Expor estado autenticado, usuario basico e acoes de entrada/saida.
- [ ] 3.5 Cobrir comportamento de sessao com testes.

## Implementation Details
Seguir a TechSpec nas secoes "Integration Points > Keycloak", "Data Flow" e "Component Overview". Esta tarefa deve depender da configuracao do client SPA criada na task_01 e da estrutura do frontend criada na task_02.

### Relevant Files
- `infra/keycloak/laboratorio-keycloak-realm.json` — define client SPA que sera usado.
- `frontend/src/servicos/servicoDeAutenticacao.ts` — servico de autenticacao a criar.
- `frontend/src/composables/useSessao.ts` — estado de sessao a criar.
- `frontend/src/main.ts` — ponto de inicializacao da autenticacao.

### Dependent Files
- `frontend/src/servicos/servicoDeUsuario.ts` — dependera do token para chamadas autenticadas.
- `frontend/src/servicos/servicoDeDocumentos.ts` — dependera do token para chamadas protegidas.
- `frontend/src/paginas/PainelDoLaboratorio.vue` — exibira login/logout e estado da sessao.

### Related ADRs
- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — exige login real via Keycloak.
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — define stack SPA.

## Deliverables
- Login e logout reais funcionando no frontend.
- Estado de sessao reutilizavel por composables e componentes.
- Token disponivel somente para chamadas autorizadas, sem exibicao indevida.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for authentication state behavior **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] `useSessao` inicia como nao autenticado quando Keycloak nao tem sessao.
  - [ ] `useSessao` expoe estado autenticado apos inicializacao simulada com sucesso.
  - [ ] `servicoDeAutenticacao` chama login e logout sem registrar token em log.
- Integration tests:
  - [ ] Botao de entrada aparece quando nao autenticado.
  - [ ] Botao de saida aparece quando autenticado.
  - [ ] Servicos autenticados recebem token quando sessao existe.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Frontend autentica com Keycloak usando client SPA publico.
- Nenhum client secret ou token bruto aparece na UI ou logs.
