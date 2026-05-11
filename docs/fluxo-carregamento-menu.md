# Fluxo de carregamento do menu dinamico

Rastreamento completo de classes e chamadas desde o login no frontend ate a resposta do backend, passando pelas chamadas ao Keycloak Admin API.

---

## 1. Inicializacao da aplicacao

```
main.ts
  └─ createApp(App).use(rotas).mount('#app')
```

O Vue monta o `App.vue`, que renderiza o `<CabecalhoPrincipal>` imediatamente.

---

## 2. CabecalhoPrincipal.vue — ponto de partida do menu

```
CabecalhoPrincipal.vue  (onMounted)
  ├─ useSessao()         → inicializarSessao()
  └─ useMenuDinamico()   → carregarMenu()  ← so se autenticado
```

O `onMounted` dispara duas coisas em sequencia: primeiro verifica a sessao, depois carrega o menu se o usuario estiver logado. Existe tambem um `watch(autenticado)` que recarrega o menu se o estado mudar depois (ex: usuario faz login sem recarregar a pagina).

---

## 3. Verificacao de sessao via Keycloak JS

```
useSessao.ts
  └─ inicializarAutenticacao()  (servicoDeAutenticacao.ts)
       └─ keycloak.init({ onLoad: 'check-sso', pkceMethod: 'S256' })
```

`servicoDeAutenticacao.ts` instancia a lib `keycloak-js` com URL, realm e clientId lidos de `configuracao.ts`. O modo `check-sso` faz um redirect silencioso para o Keycloak para verificar se ja existe sessao ativa, sem forcar o login. Se o usuario estiver logado, `keycloak.authenticated` fica `true`.

---

## 4. Busca do menu — frontend

```
useMenuDinamico.ts → carregarMenu()
  └─ buscarMenu()  (servicoDeMenu.ts)
       └─ requisitar<RespostaMenu>('/usuario/menu')  (clienteHttp.ts)
            ├─ obterToken()  → keycloak.updateToken(30) + retorna keycloak.token
            └─ fetch(`${API_URL}/usuario/menu`, { Authorization: 'Bearer <token>' })
```

`clienteHttp.ts` sempre pega um token JWT atualizado antes de cada requisicao. Esse token e o **access token do usuario**, emitido pelo Keycloak, e enviado no header `Authorization: Bearer`.

---

## 5. Backend recebe a requisicao — validacao do JWT

```
SegurancaConfig.java  (Spring Security filter chain)
  └─ oauth2ResourceServer → jwt → ConversorPermissoesJwt
       ├─ le claims do JWT: scope, realm_access.roles, resource_access.{clientId}.roles
       └─ cria JwtAuthenticationToken com nome = preferred_username
```

O Spring valida a assinatura do JWT consultando o **JWKS endpoint do Keycloak** (configurado via `spring.security.oauth2.resourceserver.jwt.issuer-uri`). O `ConversorPermissoesJwt.java` extrai as roles e cria o objeto `Authentication` com o nome do usuario.

---

## 6. UsuarioController — endpoint `/usuario/menu`

```java
// UsuarioController.java
@GetMapping("/menu")
public RespostaMenu menu(Authentication authentication) {
    return new RespostaMenu(servicoDeMenu.listarMenuDoUsuario(authentication.getName()));
}
```

O nome do usuario (`authentication.getName()`) vem do `preferred_username` extraido pelo `ConversorPermissoesJwt`.

---

## 7. ServicoDeMenuKeycloak — logica de filtragem

```java
// ServicoDeMenuKeycloak.java
public List<ItemDeMenuResposta> listarMenuDoUsuario(String nomeDoUsuario) {
    Set<String> roles = clienteKeycloakMenu.listarRolesDoUsuario(nomeDoUsuario);
    List<ItemDeMenuCatalogo> itensPermitidos = clienteKeycloakMenu.listarItensDeMenu()
            .stream()
            .filter(ItemDeMenuCatalogo::valido)
            .filter(item -> roles.contains(item.roleNecessaria()))
            .sorted(Comparator.comparing(ItemDeMenuCatalogo::ordem)...)
            .toList();

    return montarArvore(itensPermitidos);
}
```

Busca as roles do usuario e o catalogo completo de itens, filtra os itens que o usuario tem permissao e monta a arvore pai/filho.

---

## 8. ClienteKeycloakMenuAdminRest — chamadas ao Keycloak Admin API

Este e o ponto onde o backend se conecta ao Keycloak como **servico** (nao como o usuario). Sao feitas 5 chamadas em sequencia:

```
1. POST /realms/{realm}/protocol/openid-connect/token
   body: grant_type=client_credentials
         client_id=<adminClientId>
         client_secret=<adminClientSecret>
   → obtem um token administrativo (M2M, machine-to-machine)

2. GET /admin/realms/{realm}/clients?clientId={clientId}
   → descobre o UUID interno do client no Keycloak

3. GET /admin/realms/{realm}/users?username={nomeDoUsuario}&exact=true
   → descobre o UUID interno do usuario

4. GET /admin/realms/{realm}/users/{usuarioId}/role-mappings/clients/{clientUuid}/composite
   → lista todas as roles que esse usuario tem nesse client

5. GET /admin/realms/{realm}/clients/{clientUuid}/authz/resource-server/resource?type=frontend-menu
   → lista todos os resources do tipo "frontend-menu" (o catalogo de itens de menu)
```

As configuracoes (URL, realm, adminClientId, adminClientSecret) vem de `KeycloakMenuProperties.java` via `application.yml`, no prefixo `laboratorio.keycloak.menu`.

---

## 9. Volta para o frontend

```
ClienteKeycloakMenuAdminRest retorna roles e catalogo
  └─ ServicoDeMenuKeycloak filtra e monta arvore
       └─ RespostaMenu { itens: [...] }  → JSON 200

useMenuDinamico.ts: itens.value = resposta.itens
  └─ CabecalhoPrincipal.vue re-renderiza: v-for item in itensDeMenu
```

---

## Diagrama resumido

```
Browser                  Backend (Spring)           Keycloak
   │                           │                        │
   │─── keycloak.init ─────────┼───────────────────────►│ check-sso
   │◄── authenticated=true ────┼───────────────────────-│
   │                           │                        │
   │─── GET /usuario/menu ─────►                        │
   │    (Bearer <token>)       │── valida JWT (JWKS) ──►│
   │                           │◄── OK ─────────────────│
   │                           │                        │
   │                           │── client_credentials ─►│ token admin
   │                           │◄── admin_token ─────── │
   │                           │                        │
   │                           │── GET /admin/clients ─►│ uuid do client
   │                           │── GET /admin/users ───►│ uuid do usuario
   │                           │── GET /role-mappings ─►│ roles do usuario
   │                           │── GET /resources ─────►│ catalogo de menu
   │                           │                        │
   │◄── { itens: [...] } ──────│                        │
   │                           │                        │
   renderiza nav-links
```

---

## Dois tokens diferentes

O ponto mais importante do fluxo: o backend usa **dois tokens com propositos distintos**.

| Token | Quem emite | Para que serve |
|---|---|---|
| Access token do usuario | Keycloak (via login na SPA) | Autenticar quem fez a requisicao ao backend |
| Token administrativo M2M | Keycloak (via client_credentials) | Consultar a Admin API para ler roles e recursos |

O token do usuario prova a identidade de quem esta logado. O token M2M e uma credencial do proprio backend, usada para perguntar ao Keycloak quais permissoes esse usuario tem no contexto do menu.
