# TechSpec: Menu Dinamico Pelo Keycloak

## Executive Summary

A implementacao adicionara um catalogo de navegacao no Keycloak por meio de um client confidencial `laboratorio-menu`. O backend Spring Boot consultara esse catalogo via Admin REST API, filtrara os resources de menu conforme roles efetivas do usuario no client de menu e retornara uma arvore JSON para o frontend Vue. O frontend carregara essa arvore apos login, renderizara o cabecalho dinamicamente e validara rotas ficticias com uma combinacao de rotas explicitas e rota generica compartilhada.

O principal trade-off e escolher um modelo hibrido mais didatico em vez do Authorization Services completo para cada decisao de menu: resources/attributes descrevem menus, roles liberam visibilidade. Isso reduz complexidade para a V1, mantem o aprendizado claro e ainda preserva a separacao entre login, API e catalogo de navegacao.

## System Architecture

### Component Overview

- **Keycloak `laboratorio-frontend`**
  - Client publico da SPA.
  - Continua responsavel pelo login via navegador.

- **Keycloak `laboratorio-api`**
  - Client/resource server da API.
  - Continua responsavel por roles/permissoes de endpoints backend.

- **Keycloak `laboratorio-menu`**
  - Novo client confidencial.
  - Armazena resources de menu/submenu.
  - Define roles de visibilidade, como `menu:cadastros`, `menu:relatorios`, `submenu:usuarios`.

- **Backend Spring Boot**
  - Expoe `GET /usuario/menu`.
  - Usa token do usuario autenticado para identificar usuario.
  - Usa Admin REST API para consultar resources/attributes do `laboratorio-menu`.
  - Filtra menus pelas roles efetivas do usuario no client `laboratorio-menu`.
  - Devolve arvore de menu pronta, sem expor detalhes administrativos do Keycloak.

- **Frontend Vue**
  - Adiciona `useMenuDinamico`.
  - Atualiza `CabecalhoPrincipal.vue` para renderizar menu dinamico.
  - Usa rotas explicitas para telas reais/importantes.
  - Usa rota generica para telas ficticias.
  - Exibe alerta fixo de acesso negado quando rota direta nao esta liberada.

### Data Flow

1. Usuario abre SPA e faz login pelo `laboratorio-frontend`.
2. SPA obtem token e chama `GET /usuario/menu`.
3. Backend valida JWT como resource server.
4. Backend consulta o Keycloak usando credencial de servico.
5. Backend localiza resources do tipo `frontend-menu` no `laboratorio-menu`.
6. Backend resolve roles do usuario no client `laboratorio-menu`.
7. Backend filtra, ordena e monta arvore de menus.
8. Frontend renderiza a navegacao.
9. Rota ficticia permitida abre tela compartilhada de confirmacao.
10. Rota ficticia nao permitida exibe alerta e redireciona para `/laboratorio`.

## Implementation Design

### Core Interfaces

Backend:

```java
public record ItemDeMenuResposta(
        String id,
        String rotulo,
        String rota,
        String tipo,
        Integer ordem,
        String icone,
        String roleNecessaria,
        List<ItemDeMenuResposta> filhos
) {}
```

```java
public interface ServicoDeMenu {
    List<ItemDeMenuResposta> listarMenuDoUsuario(String nomeDoUsuario);
}
```

Frontend:

```ts
export type ItemDeMenu = {
  id: string;
  rotulo: string;
  rota: string;
  tipo: 'menu' | 'submenu';
  ordem: number;
  icone?: string;
  filhos: ItemDeMenu[];
};
```

### Data Models

**Resource de menu no Keycloak**

Cada resource do `laboratorio-menu` deve representar um item de navegacao.

Atributos obrigatorios:

- `id`: identificador estavel.
- `rotulo`: texto PT-BR exibido no menu.
- `rota`: rota do Vue Router.
- `tipo`: `menu` ou `submenu`.
- `ordem`: ordenacao numerica.
- `roleNecessaria`: role do client `laboratorio-menu` exigida para visibilidade.

Atributos opcionais:

- `icone`: nome de icone usado pelo frontend.
- `pai`: id do menu pai, para submenus.

Exemplo conceitual:

```json
{
  "name": "menu.cadastros.clientes",
  "type": "frontend-menu",
  "uris": ["/tela/cadastros-clientes"],
  "attributes": {
    "id": ["cadastros-clientes"],
    "rotulo": ["Clientes"],
    "tipo": ["submenu"],
    "pai": ["cadastros"],
    "ordem": ["10"],
    "roleNecessaria": ["submenu:cadastros-clientes"]
  }
}
```

**Resposta de menu**

```json
{
  "itens": [
    {
      "id": "cadastros",
      "rotulo": "Cadastros",
      "rota": "/tela/cadastros",
      "tipo": "menu",
      "ordem": 10,
      "filhos": []
    }
  ]
}
```

### API Endpoints

#### `GET /usuario/menu`

Descricao: retorna a arvore de menus permitida para o usuario autenticado.

Status:

- `200`: menu carregado.
- `401`: usuario nao autenticado.
- `502`: Keycloak indisponivel ou falha ao consultar catalogo.
- `500`: erro inesperado ao montar menu.

Resposta:

```json
{
  "itens": [
    {
      "id": "relatorios",
      "rotulo": "Relatorios",
      "rota": "/tela/relatorios",
      "tipo": "menu",
      "ordem": 30,
      "icone": "BarChart3",
      "filhos": [
        {
          "id": "relatorios-gerenciais",
          "rotulo": "Gerenciais",
          "rota": "/tela/relatorios-gerenciais",
          "tipo": "submenu",
          "ordem": 10,
          "filhos": []
        }
      ]
    }
  ]
}
```

## Integration Points

### Keycloak Admin REST API

Uso:

- Obter token de servico para o backend consultar Keycloak.
- Localizar o client `laboratorio-menu`.
- Listar resources de authorization server filtrando por `type = frontend-menu`.
- Ler attributes/uris dos resources.
- Consultar roles efetivas do usuario no client `laboratorio-menu`.

Configuracoes necessarias:

- `laboratorio.keycloak.url`
- `laboratorio.keycloak.realm`
- `laboratorio.keycloak.menu-client-id`
- `laboratorio.keycloak.admin-client-id`
- `laboratorio.keycloak.admin-client-secret`

Tratamento de erro:

- Falha de autenticacao administrativa deve gerar erro claro no backend.
- Falha de consulta ao Keycloak deve retornar mensagem didatica ao frontend.
- Backend nao deve expor secrets, tokens administrativos ou payload bruto do Keycloak.

## Impact Analysis

| Component | Impact Type | Description and Risk | Required Action |
| --- | --- | --- | --- |
| `infra/keycloak/laboratorio-keycloak-realm.json` | modified | Adiciona client `laboratorio-menu`, roles e resources de menu. Risco medio por configuracao sensivel. | Atualizar realm e validar importacao. |
| Backend `/usuario` | modified | Adiciona endpoint de menu. Risco baixo/medio por depender do Keycloak Admin REST API. | Criar controller/servico dedicado. |
| Backend configuracao | modified | Adiciona propriedades para integracao administrativa com Keycloak. Risco medio por secrets. | Usar defaults locais e nao expor secrets no frontend. |
| Frontend `CabecalhoPrincipal.vue` | modified | Troca links fixos por navegacao dinamica. Risco medio de regressao visual. | Renderizar estados carregando/erro/vazio. |
| Frontend `rotas.ts` | modified | Adiciona rota generica e guard. Risco medio para navegacao direta. | Validar acesso pelo menu carregado. |
| Frontend composables | new | Adiciona `useMenuDinamico`. Risco baixo por seguir padrao existente. | Testar estados de sucesso e erro. |

## Testing Approach

### Unit Tests

Backend:

- Servico de menu monta arvore pai/filho ordenada.
- Servico filtra itens sem role efetiva.
- Servico ignora resources sem atributos obrigatorios.
- Controller retorna `200` para usuario autenticado.
- Falha do Keycloak e convertida em erro controlado.

Frontend:

- `useMenuDinamico` carrega menu e expoe itens.
- `useMenuDinamico` trata erro.
- Guard de rota permite rota ficticia existente no menu.
- Guard de rota bloqueia rota ficticia ausente.
- Pagina ficticia mostra `Usuario {nome} acessou {rotulo} porque o menu foi liberado pelo Keycloak.`

### Integration Tests

A V1 tera foco obrigatorio em unitarios backend/frontend. Testes de integracao completos com Keycloak real podem ficar para fase posterior ou verificacao manual documentada.

## Development Sequencing

### Build Order

1. Atualizar realm com `laboratorio-menu`, roles e resources iniciais - sem dependencias.
2. Criar modelos backend de menu - depende do passo 1 para refletir atributos esperados.
3. Criar cliente backend para Admin REST API do Keycloak - depende do passo 2 para mapear dados externos para modelos internos.
4. Criar servico backend de menu e filtro por roles - depende dos passos 2 e 3.
5. Criar endpoint `GET /usuario/menu` - depende do passo 4.
6. Criar servico frontend `servicoDeMenu` - depende do passo 5.
7. Criar composable `useMenuDinamico` - depende do passo 6.
8. Atualizar `CabecalhoPrincipal.vue` para menu dinamico - depende do passo 7.
9. Adicionar rotas hibridas e pagina ficticia compartilhada - depende dos passos 7 e 8.
10. Adicionar guard/alerta de acesso negado - depende do passo 9.
11. Adicionar testes unitarios backend/frontend - depende dos passos 4, 5, 7, 9 e 10.
12. Atualizar documentacao de uso - depende de todos os passos anteriores.

### Technical Dependencies

- Keycloak local precisa importar o realm atualizado.
- Backend precisa de credencial para consultar Admin REST API.
- Frontend precisa receber token valido do `laboratorio-frontend`.
- Usuarios ficticios precisam existir no realm com roles do `laboratorio-menu`.

## Monitoring and Observability

- Logar falha ao consultar Keycloak com campos: `realm`, `clientId`, `operacao`, `status`.
- Logar quantidade de itens de menu retornados por usuario em nivel debug.
- Nao logar tokens, secrets ou payloads administrativos completos.
- No frontend, mostrar estado de erro de menu sem quebrar a aplicacao inteira.

## Technical Considerations

### Key Decisions

- **Client dedicado `laboratorio-menu`:** separa login, API e catalogo de navegacao.
- **Modelo hibrido resources + roles:** resources guardam metadados; roles liberam visibilidade.
- **Admin REST API:** backend consulta catalogo configurado no Keycloak.
- **Composable simples:** segue padrao existente do frontend.
- **Rotas hibridas:** mantem rotas importantes explicitas e usa rota generica para telas ficticias.

### Known Risks

- **Admin REST API pode exigir permissoes amplas demais.** Mitigar com service account de menor privilegio possivel.
- **Atributos de resources podem ficar inconsistentes.** Mitigar com validacao e logs claros.
- **Menu pode ser confundido com seguranca real.** Mitigar com documentacao e mensagens didaticas.
- **Acesso direto pode ocorrer antes do menu carregar.** Mitigar carregando o menu antes de decidir o guard.
- **Falha do Keycloak pode deixar menu indisponivel.** Mitigar com estado de erro claro no cabecalho/painel.

## Architecture Decision Records

- [ADR-001: Menu Dinamico A Partir Do Client Frontend No Keycloak](adrs/adr-001.md) — Decide criar client confidencial dedicado ao catalogo de menu/submenu.
- [ADR-002: Menu Corporativo Didatico Com Controle De Rota](adrs/adr-002.md) — Decide usar menus corporativos ficticios, rotas reais e alerta de acesso negado.
- [ADR-003: Catalogo Hibrido Com Resources E Roles No Client De Menu](adrs/adr-003.md) — Decide usar resources/attributes para catalogo e roles para visibilidade.
- [ADR-004: Integracao Backend Keycloak Via Admin REST API](adrs/adr-004.md) — Decide consultar o catalogo pelo backend usando Admin REST API.
- [ADR-005: Composable Simples E Rotas Hibridas No Frontend](adrs/adr-005.md) — Decide usar `useMenuDinamico` e rota generica para telas ficticias.
