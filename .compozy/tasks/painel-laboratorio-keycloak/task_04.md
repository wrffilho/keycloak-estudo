---
status: completed
title: Implementar servicos de usuario e permissoes
type: frontend
complexity: medium
dependencies:
  - task_03
---

# Task 04: Implementar servicos de usuario e permissoes

## Overview
Esta tarefa conecta o frontend aos endpoints de usuario do backend e cria a base de permissoes da interface. Ela permite que a UI mostre quem esta autenticado e quais acoes devem aparecer disponiveis ou bloqueadas.

<critical>
- ALWAYS READ the PRD and TechSpec before starting
- REFERENCE TECHSPEC for implementation details — do not duplicate here
- FOCUS ON "WHAT" — describe what needs to be accomplished, not how
- MINIMIZE CODE — show code only to illustrate current structure or problem areas
- TESTS REQUIRED — every task MUST include tests in deliverables
</critical>

<requirements>
- MUST consumir `/usuario/perfil` com sessao autenticada.
- MUST consumir `/usuario/permissoes` com sessao autenticada.
- MUST modelar permissoes `documentos:ler`, `documentos:criar`, `documentos:editar` e `documentos:aprovar`.
- MUST expor helpers de permissao para a UI, como leitura, criacao, edicao e aprovacao.
- SHOULD fornecer mensagens didaticas para permissoes ausentes.
</requirements>

## Subtasks
- [ ] 4.1 Criar tipos de seguranca e permissoes.
- [ ] 4.2 Criar `servicoDeUsuario.ts`.
- [ ] 4.3 Criar `usePermissoes`.
- [ ] 4.4 Integrar perfil e permissoes ao estado de sessao.
- [ ] 4.5 Testar helpers de permissoes e respostas do servico.

## Implementation Details
Seguir a TechSpec nas secoes "Core Interfaces", "API Endpoints > Usuario" e "Component Overview". Esta tarefa deve usar o token exposto pela task_03 e nao deve implementar telas completas ainda.

### Relevant Files
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/UsuarioController.java` — contrato dos endpoints de usuario.
- `backend/src/main/java/br/com/wanderlei/keycloakestudo/seguranca/LeitorDePermissoesSpringSecurity.java` — fonte backend das permissoes efetivas.
- `frontend/src/tipos/seguranca.ts` — tipos de perfil e permissoes.
- `frontend/src/servicos/servicoDeUsuario.ts` — servico de usuario a criar.
- `frontend/src/composables/usePermissoes.ts` — composable de permissoes a criar.

### Dependent Files
- `frontend/src/composables/useDocumentos.ts` — dependera dos helpers de permissao.
- `frontend/src/componentes/laboratorio/*` — exibira usuario e permissoes.
- `frontend/src/paginas/PainelDoLaboratorio.vue` — consumira permissao e perfil.

### Related ADRs
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — exige permissoes visiveis sem foco no token.
- [ADR-004: Organizacao do Frontend por Composables e Servicos de Dominio](adrs/adr-004.md) — define servicos e composables por dominio.

## Deliverables
- Servico de usuario consumindo perfil e permissoes.
- Composable de permissoes com helpers claros.
- Tipos de seguranca em PT-BR.
- Unit tests with 80%+ coverage **(REQUIRED)**
- Integration tests for user and permissions loading **(REQUIRED)**

## Tests
- Unit tests:
  - [ ] Usuario com `documentos:ler` pode ler e nao pode criar se essa permissao faltar.
  - [ ] Usuario com `documentos:criar` pode criar documento.
  - [ ] Usuario sem `documentos:aprovar` recebe helper falso para aprovacao.
  - [ ] Servico de usuario interpreta resposta `{ permissoes: [...] }` corretamente.
- Integration tests:
  - [ ] Painel de permissoes carrega dados apos sessao autenticada simulada.
  - [ ] Erro `401` ao carregar usuario gera estado explicavel para a UI.
- Test coverage target: >=80%
- All tests must pass

## Success Criteria
- All tests passing
- Test coverage >=80%
- Frontend conhece permissoes efetivas do usuario autenticado.
- Componentes futuros conseguem saber quais acoes bloquear.
