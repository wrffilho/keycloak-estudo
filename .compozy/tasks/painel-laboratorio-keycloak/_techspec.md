# TechSpec: Painel do Laboratorio Keycloak

## Executive Summary

A V1 sera implementada como um novo modulo `frontend/` na raiz do monolito modular, usando Vue, TypeScript, Vite e Vue Router. O frontend consumira o backend Spring Boot existente, fara login real pelo Keycloak com um client SPA publico separado e mostrara documentos, permissoes efetivas, acoes bloqueadas e diagnostico da ultima acao relevante.

O principal trade-off tecnico e manter o frontend simples e didatico, sem store global pesada nem wizard rigido, enquanto ainda suporta autenticacao real, permissoes e uso livre. A solucao escolhida usa servicos de dominio para chamadas HTTP e composables Vue para estado e regras de tela. Isso preserva legibilidade PT-BR-first e evita arquitetura excessiva para um laboratorio.

## System Architecture

### Component Overview

- **`frontend/`**
  - Novo modulo SPA.
  - Responsavel por UI, login Keycloak, consumo do backend, permissoes visuais e diagnostico de acoes.
  - Usa Vue Router para separar rotas pequenas do laboratorio sem criar um portal documental completo.
  - Deve usar nomes em PT-BR para arquivos, funcoes, componentes e variaveis quando fizer sentido.

- **Keycloak**
  - Continua subindo por Docker.
  - Deve ter um client SPA publico separado do `laboratorio-api`.
  - Responsavel por login/logout e emissao de tokens para o frontend.

- **Backend Spring Boot**
  - Continua como Resource Server.
  - Valida JWT e aplica permissoes reais nos endpoints.
  - Continua sendo a fonte de verdade da autorizacao.

- **Servicos de dominio do frontend**
  - `servicoDeAutenticacao.ts`: inicializacao Keycloak, login, logout, token atual.
  - `servicoDeUsuario.ts`: chama `/usuario/perfil` e `/usuario/permissoes`.
  - `servicoDeDocumentos.ts`: chama endpoints de documentos.

- **Composables Vue**
  - `useSessao`: usuario autenticado e acoes de login/logout.
  - `usePermissoes`: permissoes efetivas e helpers como `podeCriarDocumento`.
  - `useDocumentos`: lista, detalhe, criar, editar e aprovar.
  - `useDiagnosticoDeAcao`: ultima acao, endpoint, metodo, status e mensagem.

### Data Flow

1. Usuario abre o frontend.
2. Frontend inicializa Keycloak.
3. Usuario entra pelo Keycloak.
4. Frontend obtem estado autenticado e chama `/usuario/perfil` e `/usuario/permissoes`.
5. UI mostra usuario, permissoes e documentos.
6. Ao executar uma acao permitida, o servico de dominio chama o backend com Bearer token.
7. `useDiagnosticoDeAcao` registra acao, endpoint e status.
8. Se a acao nao for permitida pela permissao conhecida, o botao fica desabilitado com explicacao.
9. Se o backend retornar `401` ou `403`, a tela mostra mensagem didatica.

## Implementation Design

### Core Interfaces

```ts
export type Permissao =
  | 'documentos:ler'
  | 'documentos:criar'
  | 'documentos:editar'
  | 'documentos:aprovar';

export type Documento = {
  id: string;
  titulo: string;
  conteudo: string;
  status: 'RASCUNHO' | 'PUBLICADO' | 'APROVADO';
};
```

```ts
export type DiagnosticoDeAcao = {
  acao: string;
  metodo: 'GET' | 'POST' | 'PUT';
  endpoint: string;
  status?: number;
  mensagem: string;
  permitido: boolean;
};
```

```ts
export type PerfilUsuario = {
  usuario: string;
  autenticado: boolean;
};

export type RespostaPermissoes = {
  permissoes: Permissao[];
};
```

### Frontend Structure

```text
frontend/
  src/
    componentes/
      documentos/
      laboratorio/
      layout/
    composables/
      useSessao.ts
      usePermissoes.ts
      useDocumentos.ts
      useDiagnosticoDeAcao.ts
    servicos/
      servicoDeAutenticacao.ts
      servicoDeUsuario.ts
      servicoDeDocumentos.ts
    tipos/
      documento.ts
      seguranca.ts
      diagnostico.ts
    paginas/
      PainelDoLaboratorio.vue
      Documentos.vue
      DocumentoDetalhe.vue
    rotas/
      rotas.ts
    App.vue
    main.ts
```

### Frontend Routes

- `/`
  - Redireciona para `/laboratorio`.

- `/laboratorio`
  - Tela principal do painel, com sessao, permissoes, diagnostico e entrada para documentos.

- `/documentos`
  - Lista documentos e mostra acoes permitidas ou bloqueadas conforme permissoes.

- `/documentos/:id`
  - Mostra detalhe do documento e acoes de editar/aprovar conforme permissoes.

### Data Models

- **Documento**
  - `id: string`
  - `titulo: string`
  - `conteudo: string`
  - `status: RASCUNHO | PUBLICADO | APROVADO`

- **CriarDocumento**
  - `titulo: string`
  - `conteudo: string`

- **EditarDocumento**
  - `titulo: string`
  - `conteudo: string`

- **PerfilUsuario**
  - `usuario: string`
  - `autenticado: boolean`

- **Permissao**
  - `documentos:ler`
  - `documentos:criar`
  - `documentos:editar`
  - `documentos:aprovar`

- **DiagnosticoDeAcao**
  - `acao`
  - `metodo`
  - `endpoint`
  - `status`
  - `mensagem`
  - `permitido`

### API Endpoints

#### Usuario

- `GET /usuario/perfil`
  - Usado por `servicoDeUsuario`.
  - Retorna usuario autenticado.
  - Possiveis status: `200`, `401`.

- `GET /usuario/permissoes`
  - Usado por `servicoDeUsuario`.
  - Retorna permissoes efetivas.
  - Possiveis status: `200`, `401`.

#### Documentos

- `GET /documentos`
  - Permissao esperada: `documentos:ler`.
  - Atualiza lista de documentos.

- `GET /documentos/{id}`
  - Permissao esperada: `documentos:ler`.
  - Atualiza detalhe do documento.

- `POST /documentos`
  - Permissao esperada: `documentos:criar`.
  - Cria documento.

- `PUT /documentos/{id}`
  - Permissao esperada: `documentos:editar`.
  - Edita documento.

- `POST /documentos/{id}/aprovar`
  - Permissao esperada: `documentos:aprovar`.
  - Aprova documento.

## Integration Points

### Keycloak

- Criar client SPA publico, por exemplo `laboratorio-frontend`.
- Nao usar client secret no frontend.
- Usar login real via Keycloak.
- Frontend envia access token no header `Authorization: Bearer ...`.
- Redirect URI deve apontar para o endereco do frontend em desenvolvimento e Docker.
- O client `laboratorio-api` continua sendo usado pelo backend para converter permissoes do token.

### Backend

- O backend pode precisar de CORS para permitir chamadas do frontend.
- O backend continua validando autorizacao por `@PreAuthorize`.
- O frontend usa `/usuario/permissoes` como fonte para estados visuais, mas nao substitui a validacao do backend.

### Docker Compose

- Adicionar servico `frontend`.
- `docker compose up -d --build` deve subir Keycloak, backend e frontend.
- Manter comando separado para desenvolvimento local do frontend.

### GetDesign Lovable

- Durante implementacao do frontend, executar:
  ```bash
  npx getdesign@latest add lovable
  ```
- O resultado deve ser adaptado ao vocabulario e UX do laboratorio, nao usado como tema generico.

## Impact Analysis

| Component | Impact Type | Description and Risk | Required Action |
|---|---|---|---|
| `frontend/` | new | Novo modulo Vue/TS com Vue Router e dependencias Node | Criar estrutura, scripts e Dockerfile |
| `docker-compose.yml` | modified | Novo servico frontend e possivel ajuste de portas | Adicionar servico e documentar comandos |
| `infra/keycloak/laboratorio-keycloak-realm.json` | modified | Novo client SPA publico | Adicionar client sem secret e redirect URIs especificas |
| `backend/src/main/java/.../SegurancaConfig.java` | modified | Pode precisar liberar CORS para frontend | Adicionar configuracao CORS se necessario |
| `README.md` | modified | Novos comandos de frontend e fluxo completo | Atualizar secao de execucao |
| `backend` controllers | unchanged | Endpoints ja cobrem o MVP | Reusar contratos existentes |

## Testing Approach

### Unit Tests

- Testar `usePermissoes`:
  - usuario com `documentos:ler`;
  - usuario com `documentos:criar`;
  - usuario sem permissao para acao;
  - helpers retornam `true`/`false` corretamente.

- Testar `servicoDeDocumentos`:
  - monta endpoints corretos;
  - propaga status de erro;
  - envia payload correto para criar/editar.

- Testar `useDiagnosticoDeAcao`:
  - registra acao permitida;
  - registra erro `401`;
  - registra erro `403`;
  - mantem mensagem didatica.

### Component Tests

- Botoes de criar/editar/aprovar ficam desabilitados sem permissao.
- Explicacao de permissao ausente aparece junto da acao bloqueada.
- Painel mostra usuario e permissoes apos carregamento.
- Diagnostico exibe ultima acao relevante.

### Integration Tests

- Verificar frontend rodando contra backend e Keycloak no Docker.
- Fluxo manual minimo:
  - login como `leitor`;
  - listar documentos;
  - observar criar/editar/aprovar bloqueados;
  - login como `editor`;
  - criar e editar;
  - login como `aprovador`;
  - aprovar.

## Development Sequencing

### Build Order

1. **Preparar client SPA no realm** - no dependencies.
2. **Adicionar configuracao CORS no backend se necessario** - depends on step 1 to know frontend origins.
3. **Criar modulo `frontend/` com Vue, TypeScript e Vite** - depends on step 1 for auth configuration values.
4. **Aplicar base visual GetDesign Lovable** - depends on step 3.
5. **Implementar `servicoDeAutenticacao`** - depends on steps 1 and 3.
6. **Implementar `servicoDeUsuario` e tipos de seguranca** - depends on step 5.
7. **Implementar `useSessao` e `usePermissoes`** - depends on steps 5 and 6.
8. **Implementar `servicoDeDocumentos` e tipos de documentos** - depends on step 3.
9. **Implementar `useDocumentos`** - depends on steps 7 and 8.
10. **Implementar `useDiagnosticoDeAcao`** - depends on steps 8 and 9.
11. **Criar componentes de documentos e painel de permissoes** - depends on steps 7, 9 and 10.
12. **Criar `PainelDoLaboratorio.vue`** - depends on step 11.
13. **Adicionar Dockerfile do frontend e servico no Compose** - depends on step 12.
14. **Adicionar testes unitarios e de componentes** - depends on steps 7 through 12.
15. **Atualizar README** - depends on steps 13 and 14.

### Technical Dependencies

- Node.js disponivel para desenvolvimento frontend.
- Acesso ao registry npm para instalar dependencias e rodar GetDesign.
- Keycloak importando realm atualizado.
- Backend disponivel em `http://localhost:8081`.
- Keycloak disponivel em `http://localhost:8080`.

## Monitoring and Observability

- Exibir no frontend o diagnostico da ultima acao:
  - acao;
  - endpoint;
  - status;
  - mensagem didatica.
- Logar no console apenas erros inesperados em desenvolvimento.
- Nao registrar token bruto em logs.
- Em Docker, logs padrao do frontend devem indicar que o servidor iniciou e em qual porta.

## Technical Considerations

### Key Decisions

- **Vue + TypeScript + Vite + Vue Router**
  - Rationale: SPA leve, didatica e simples para componentes e rotas pequenas.
  - Trade-off: nova stack no repositorio.
  - Alternatives rejected: React, Angular.

- **Composables em vez de store central**
  - Rationale: reduz peso arquitetural e mantem codigo didatico.
  - Trade-off: exige disciplina para nao duplicar estado.
  - Alternatives rejected: store global inicial.

- **Servicos por dominio**
  - Rationale: alinha com monolito modular e PT-BR-first.
  - Trade-off: mais arquivos iniciais.
  - Alternatives rejected: cliente API unico.

- **Acoes sem permissao desabilitadas**
  - Rationale: ensina sem induzir falha constante.
  - Trade-off: o usuario ve menos respostas `403` espontaneas.
  - Mitigation: tratar `403` do backend e explicar quando ocorrer.

- **Jornada contextual em vez de wizard rigido**
  - Rationale: usuario entra e usa normalmente, com apoio didatico.
  - Trade-off: menos controle sobre sequencia de aprendizado.
  - Mitigation: painel de diagnostico e explicacoes curtas.

### Known Risks

- **CORS bloquear chamadas do frontend**
  - Mitigation: adicionar configuracao CORS explicita no backend.

- **Client SPA nao receber permissoes esperadas**
  - Mitigation: validar mapeamento do realm e manter backend usando permissoes aceitas pelo `ConversorPermissoesJwt`.

- **PT-BR no codigo ficar artificial**
  - Mitigation: usar PT-BR para dominio do projeto e manter nomes proprios de APIs externas.

- **Design visual esconder proposito didatico**
  - Mitigation: priorizar painel de permissoes e diagnostico sobre decoracao.

- **Testes com Keycloak real ficarem pesados**
  - Mitigation: MVP automatiza testes unitarios/componentes e mantem roteiro manual para integracao completa.

## Architecture Decision Records

- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — Define a V1 como painel didatico, com documentos reais, diagnostico HTTP e client SPA proprio.
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — Define o MVP com jornada guiada e uso livre de documentos.
- [ADR-003: Stack do Frontend com Vue, TypeScript, Vite, Vue Router e GetDesign Lovable](adrs/adr-003.md) — Define a stack tecnica do modulo frontend.
- [ADR-004: Organizacao do Frontend por Composables e Servicos de Dominio](adrs/adr-004.md) — Define composables Vue e servicos por dominio como organizacao principal.
