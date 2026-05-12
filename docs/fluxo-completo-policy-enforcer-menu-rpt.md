# Fluxo Completo: Login, Menu, RPT E Policy Enforcer

Este guia mostra o ciclo realista desta branch: o frontend autentica o usuario e chama a API com access token comum; o backend assume a autorizacao fina com o Policy Enforcer antes de executar o controller.

Resumo:

```text
Frontend autentica.
Frontend chama API com access token comum.
Backend valida o token.
Policy Enforcer consulta o Keycloak quando a rota exige autorizacao fina.
Policy Enforcer valida recurso/escopo.
Controller executa.
```

## Mapa De Classes

| Camada | Arquivo/classe | Responsabilidade |
|---|---|---|
| Frontend | `servicoDeAutenticacao.ts` | Login com `keycloak-js`, renovacao e entrega do access token comum |
| Frontend | `clienteHttp.ts` | Envia `Authorization: Bearer <access-token-comum>` para a API |
| Frontend | `servicoDeDocumentos.ts` | Chama `/documentos`; nao conhece RPT nem UMA |
| Frontend | `useMenuDinamico.ts` | Carrega menu dinamico |
| Backend | `SegurancaConfig.java` | Valida JWT com Spring Resource Server e exige autenticacao |
| Backend | `ConversorPermissoesJwt.java` | Converte claims do token em authorities para diagnostico e endpoints sem enforcer |
| Backend | `UsuarioController.java` | Expoe perfil, permissoes e menu |
| Backend | `ClienteKeycloakMenuAdminRest.java` | Vai ao Keycloak Admin REST para montar o menu |
| Backend | `PolicyEnforcerConfig.java` | Registra filtros e configura paths/metodos/escopos do Policy Enforcer |
| Backend | `PolicyEnforcerProperties.java` | Faz binding do mapa de rotas declarado no `application.yml` |
| Backend | `ServletPolicyEnforcerFilter` | Filtro do Keycloak que consulta o Authorization Services e aplica a autorizacao fina |
| Backend | `DocumentosController.java` | Executa a operacao depois que o enforcer libera |

## Etapa 1: Login

O usuario clica **Entrar com Keycloak**.

```text
useSessao.entrar()
  -> servicoDeAutenticacao.entrar()
  -> keycloak.login()
  -> Keycloak autentica o usuario
  -> frontend recebe access token comum
```

O frontend usa o client publico:

```text
clientId: laboratorio-frontend
realm: laboratorio-keycloak
```

Nesse ponto ainda nao existe RPT visivel para o frontend. Existe apenas o access token comum do usuario.

## Etapa 2: Carregamento Do Menu

Quando o usuario esta autenticado, o cabecalho carrega o menu:

```text
CabecalhoPrincipal.vue
  -> useMenuDinamico.carregarMenu()
  -> servicoDeMenu.buscarMenu()
  -> clienteHttp.requisitar('/usuario/menu')
```

O frontend envia:

```http
GET /usuario/menu
Authorization: Bearer <access-token-comum>
```

O backend valida esse token no Spring Resource Server.

Depois `UsuarioController.menu()` chama:

```text
ServicoDeMenuKeycloak.listarMenuDoUsuario(authentication.getName())
```

## Etapa 3: Backend Vai Ao Keycloak Para Montar Menu

Menu dinamico e carregado pelo backend.

A classe que fala com o Keycloak e:

```text
ClienteKeycloakMenuAdminRest.java
```

Ela obtem token administrativo com client credentials:

```text
client_id=laboratorio-menu
client_secret=laboratorio-menu-secret
grant_type=client_credentials
```

Depois consulta o Admin REST:

```text
1. busca o client laboratorio-menu
2. busca recursos type=frontend-menu
3. busca roles de menu do usuario
4. ServicoDeMenuKeycloak filtra os itens permitidos
```

Importante:

```text
Menu mostra caminho visual.
Menu nao autoriza endpoint de backend.
```

## Etapa 4: Usuario Clica Em Documentos

O frontend nao pede RPT.

Ele chama a API normalmente:

```text
Documentos.vue
  -> useDocumentos.carregarDocumentos()
  -> servicoDeDocumentos.listarDocumentos()
  -> clienteHttp.requisitar('/documentos')
```

Requisicao enviada:

```http
GET /documentos
Authorization: Bearer <access-token-comum>
```

Essa e a realidade desejada neste projeto: o frontend nao conhece `documentos#ler`, UMA ou RPT.

## Etapa 5: Spring Autentica A Requisicao

Antes de qualquer autorizacao fina, o Spring valida o access token comum:

```text
SegurancaConfig.java
  -> oauth2ResourceServer(jwt)
  -> ConversorPermissoesJwt
  -> SecurityContextHolder
```

Se o token estiver ausente ou invalido:

```text
401 Unauthorized
```

Se o token for valido, a requisicao segue.

## Etapa 6: Backend Descobre Qual Permissao A Rota Exige

`PolicyEnforcerConfig.java` centraliza o mapa de rotas protegidas:

| Requisicao | Permissao UMA |
|---|---|
| `GET /documentos` | `documentos#ler` |
| `POST /documentos` | `documentos#criar` |
| `GET /documentos/*` | `documentos#ler` |
| `PUT /documentos/*` | `documentos#editar` |
| `POST /documentos/*` | `documentos#aprovar` |

Hoje `POST /documentos/*` cobre a rota `POST /documentos/{id}/aprovar`. Se outra subrota `POST /documentos/...` surgir, ela deve ganhar um path/escopo proprio.

Cada entrada vem do `application.yml`:

```text
recurso: documentos
caminho: /documentos
metodo: GET
escopo: ler
permissao: documentos#ler
```

## Etapa 7: Policy Enforcer Consulta O Keycloak

A classe registrada pelo backend e:

```text
ServletPolicyEnforcerFilter
```

Ela usa a configuracao criada em `PolicyEnforcerConfig.java`:

```text
auth-server-url: http://localhost:8080
realm: laboratorio-keycloak
resource: laboratorio-api
credentials.secret: laboratorio-api-secret
paths/metodos/escopos: application.yml
```

Aqui quem pergunta ao Keycloak se pode acessar o recurso e o **backend**, por meio do filtro do Keycloak.

O Keycloak avalia:

```text
resource server: laboratorio-api
recurso: documentos
escopo: ler
permissao: Permissao para ler documentos
politica: Somente leitores
role exigida: documentos:ler
usuario: leitor
```

Se o usuario puder, o Keycloak emite um RPT.

Se nao puder, o Keycloak nega e o backend retorna `403`.

## Etapa 8: Policy Enforcer Aplica A Autorizacao

O filtro do Keycloak:

```text
ServletPolicyEnforcerFilter
```

Ele foi registrado em:

```text
PolicyEnforcerConfig.java
```

O enforcer confere:

```text
GET /documentos exige documentos#ler
usuario autenticado pode receber permissao documentos#ler
```

Se a permissao existir, libera.

Se nao existir, retorna `403`.

## Etapa 9: Controller Executa

So depois disso a chamada chega no controller:

```text
DocumentosController.listar()
  -> ServicoDeDocumentos.listar()
```

`DocumentosController.java` nao tem `@PreAuthorize` nesta branch porque a autorizacao fina acontece antes, no Policy Enforcer.

## Fluxo Do Usuario Leitor

### Abrir Documentos

```text
1. leitor esta logado no frontend
2. frontend chama GET /documentos com access token comum
3. Spring valida token
4. ServletPolicyEnforcerFilter identifica GET /documentos -> documentos#ler
5. Keycloak ve que leitor possui documentos:ler
6. ServletPolicyEnforcerFilter libera
7. DocumentosController.listar() executa
```

### Criar Documento

```text
1. leitor chama POST /documentos com access token comum
2. ServletPolicyEnforcerFilter identifica POST /documentos -> documentos#criar
3. Keycloak ve que leitor nao possui documentos:criar
4. ServletPolicyEnforcerFilter bloqueia
5. backend retorna 403
6. controller nao executa
```

## Quem Vai Ao Keycloak?

| Momento | Quem chama Keycloak | Objetivo |
|---|---|---|
| Login | Frontend com `keycloak-js` | Autenticar usuario |
| Menu | Backend com `ClienteKeycloakMenuAdminRest` | Buscar catalogo e roles de menu |
| Autorizacao fina | Backend com `ServletPolicyEnforcerFilter` | Perguntar se usuario pode acessar recurso/escopo |
| Validacao JWT | Spring Resource Server | Validar assinatura/issuer/chaves do token |
| Policy Enforcer | `ServletPolicyEnforcerFilter` | Conferir se o bearer atual carrega a permissao exigida |

## Onde Procurar Quando Falhar

### Menu Nao Carrega

- `CabecalhoPrincipal.vue`
- `useMenuDinamico.ts`
- `servicoDeMenu.ts`
- `UsuarioController.menu()`
- `ServicoDeMenuKeycloak.java`
- `ClienteKeycloakMenuAdminRest.java`
- roles no client `laboratorio-menu`

### Documento Nao Carrega

- `servicoDeDocumentos.ts`: deve enviar access token comum, sem RPT
- `PolicyEnforcerConfig.java`: deve registrar o filtro e configurar path/metodo/escopo
- `PolicyEnforcerProperties.java`: deve receber o mapa do `application.yml`
- roles no client `laboratorio-api`
- politicas do recurso `documentos`
- logs do backend

### `401`

Normalmente e falha de autenticacao:

- token ausente;
- token expirado;
- issuer errado;
- realm/URL divergente.

### `403`

Normalmente e falha de autorizacao:

- Keycloak negou o RPT;
- usuario nao possui role exigida;
- rota foi mapeada para escopo errado;
- autorizacao emitida pelo Keycloak nao contem a permissao exigida pelo enforcer.

### `ProtectedResource.find` Com `403`

Normalmente e problema de configuracao do service account do `laboratorio-api`:

- `serviceAccountsEnabled` precisa estar `true`;
- `service-account-laboratorio-api` precisa da role `laboratorio-api/uma_protection`;
- o secret do backend precisa bater com o secret do client.

### Autorizacao Negada No Docker Por Hostname

No Docker, existe um detalhe importante: o access token emitido para o navegador usa issuer:

```text
http://localhost:8080/realms/laboratorio-keycloak
```

O backend, porem, fala com o Keycloak pela rede interna em `http://keycloak:8080`. Para isso funcionar, o Keycloak precisa anunciar o hostname publico e aceitar backchannel dinamico.

Por isso, no perfil Docker:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/laboratorio-keycloak
          jwk-set-uri: http://keycloak:8080/realms/laboratorio-keycloak/protocol/openid-connect/certs

laboratorio:
  keycloak:
    policy-enforcer:
      auth-server-url: http://keycloak:8080
```

E o `docker-compose.yml` configura o Keycloak com:

```yaml
environment:
  KC_HOSTNAME: http://localhost:8080
  KC_HOSTNAME_BACKCHANNEL_DYNAMIC: "true"
```

Assim o token continua tendo issuer publico para o navegador, e o backend consegue usar o endereco interno do container.

## Frase Guia

```text
Frontend autentica e chama a API.
Backend pergunta ao Keycloak se pode.
Policy Enforcer libera ou bloqueia.
Controller so executa depois.
```
