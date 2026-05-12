# Spring Resource Server vs Policy Enforcer

Este laboratorio compara duas formas de proteger a mesma API.

- **Spring Resource Server puro**: o Spring valida o JWT e o codigo decide acesso com authorities, por exemplo `@PreAuthorize`.
- **Policy Enforcer**: o Spring valida o JWT, o filtro do Keycloak consulta o Authorization Services e aplica a autorizacao fina antes do controller.

## Diferenca Principal

| Pergunta | Spring Resource Server | Policy Enforcer nesta branch |
|---|---|---|
| Frontend envia o que para a API? | Access token comum | Access token comum |
| Quem consulta o Keycloak para autorizacao fina? | Ninguem | Backend, por meio do `ServletPolicyEnforcerFilter` |
| Quem valida o JWT? | Spring Resource Server | Spring Resource Server |
| Quem decide `GET /documentos`? | Spring, via authority no codigo | Keycloak Authorization Services + Policy Enforcer |
| Onde aparece a regra? | `@PreAuthorize("hasAuthority('documentos:ler')")` | `PolicyEnforcerConfig.java`, no mapa path/metodo/escopo |
| O controller conhece a permissao? | Sim | Nao; se chegou nele, o enforcer ja liberou |

## Conceitos

### Access Token Comum

E o token que o frontend recebe depois do login no Keycloak.

Ele prova:

```text
este usuario esta autenticado
```

O frontend envia esse token para a API:

```http
Authorization: Bearer <access-token-comum>
```

### RPT

RPT significa Requesting Party Token.

Ele prova:

```text
este usuario foi autorizado pelo Keycloak para um recurso/escopo especifico
```

Exemplo:

```text
documentos#ler
```

Nesta branch, qualquer troca UMA/RPT e detalhe interno do Policy Enforcer no backend. O frontend nao pede RPT e nao conhece UMA.

## Fluxo Com Spring Resource Server

```text
Frontend
  -> envia access token comum
Spring Resource Server
  -> valida JWT
ConversorPermissoesJwt
  -> transforma claims em authorities
@PreAuthorize
  -> compara authority exigida
Controller
  -> executa se autorizado
```

Exemplo:

```java
@PreAuthorize("hasAuthority('documentos:criar')")
```

Para `POST /documentos`:

| Situacao | Resultado |
|---|---|
| Sem token | `401` |
| Token invalido | `401` |
| Token valido sem `documentos:criar` | `403` |
| Token valido com `documentos:criar` | `201` |

## Fluxo Com Policy Enforcer

```text
Frontend
  -> envia access token comum
Spring Resource Server
  -> valida JWT
ServletPolicyEnforcerFilter
  -> identifica path/metodo configurado em PolicyEnforcerConfig.java
  -> consulta o Keycloak Authorization Services
  -> avalia recurso, escopo, permissao, politica e role
  -> libera ou bloqueia antes do controller
Controller
  -> executa se autorizado
```

## Mapa De Autorizacao Fina

O backend centraliza o mapa em `PolicyEnforcerConfig.java`:

| Endpoint | Metodo | Recurso | Escopo | Permissao UMA |
|---|---|---|---|---|
| `/documentos` | GET | `documentos` | `ler` | `documentos#ler` |
| `/documentos` | POST | `documentos` | `criar` | `documentos#criar` |
| `/documentos/*` | GET | `documentos` | `ler` | `documentos#ler` |
| `/documentos/*` | PUT | `documentos` | `editar` | `documentos#editar` |
| `/documentos/*` | POST | `documentos` | `aprovar` | `documentos#aprovar` |

Observacao: hoje `POST /documentos/*` representa a rota `POST /documentos/{id}/aprovar`. Se novas subrotas `POST /documentos/...` forem criadas, elas precisam de um path/escopo proprio para nao herdar `aprovar` por acidente.

## Quem Faz O Que No Backend

| Classe | Papel |
|---|---|
| `SegurancaConfig.java` | Configura autenticação JWT no Spring |
| `ConversorPermissoesJwt.java` | Converte claims em authorities para diagnostico e endpoints sem enforcer |
| `PolicyEnforcerConfig.java` | Registra filtros e configura paths do enforcer |
| `PolicyEnforcerProperties.java` | Faz binding das rotas protegidas declaradas no `application.yml` |
| `ServletPolicyEnforcerFilter` | Filtro do Keycloak que consulta o Authorization Services e aplica a decisao |
| `DocumentosController.java` | Executa a regra de negocio depois da autorizacao |

## Exemplo: Leitor Abrindo Documentos

```text
1. frontend chama GET /documentos com access token comum
2. Spring valida o token
3. ServletPolicyEnforcerFilter mapeia GET /documentos -> documentos#ler
4. Keycloak ve que leitor tem documentos:ler
5. ServletPolicyEnforcerFilter libera a chamada
6. DocumentosController.listar() executa
7. resposta 200
```

## Exemplo: Leitor Criando Documento

```text
1. frontend chama POST /documentos com access token comum
2. Spring valida o token
3. ServletPolicyEnforcerFilter mapeia POST /documentos -> documentos#criar
4. Keycloak ve que leitor nao tem documentos:criar
5. ServletPolicyEnforcerFilter bloqueia a chamada
6. backend retorna 403
7. DocumentosController.criar() nao executa
```

## Como Ler `401` E `403`

`401` e autenticacao:

- sem token;
- token expirado;
- issuer errado;
- assinatura invalida.

`403` e autorizacao:

- Keycloak negou RPT;
- RPT nao tem permissao exigida;
- usuario nao possui a role exigida pela politica;
- path/metodo foi mapeado para escopo errado.

## Erro Comum: `ProtectedResource.find` Com `403`

Se aparecer:

```text
org.keycloak.authorization.client.util.HttpResponseException:
Unexpected response from server: 403 / Forbidden
...
ProtectedResource.find
```

o problema geralmente nao e o usuario final. E o backend tentando consultar recursos protegidos sem permissao suficiente.

O client `laboratorio-api` precisa:

- ser confidencial;
- ter `serviceAccountsEnabled: true`;
- ter secret correto;
- ter Authorization Services habilitado;
- ter `service-account-laboratorio-api` com role `laboratorio-api/uma_protection`.

## Detalhe Docker: Host Do Token Endpoint

O access token do navegador tem issuer `http://localhost:8080/realms/laboratorio-keycloak`.

No Docker, o access token do navegador tem issuer `http://localhost:8080/realms/laboratorio-keycloak`, enquanto o backend fala com o Keycloak pela rede interna em `http://keycloak:8080`.

Para esse cenario funcionar, o compose configura o Keycloak com hostname publico e backchannel dinamico:

```text
KC_HOSTNAME=http://localhost:8080
KC_HOSTNAME_BACKCHANNEL_DYNAMIC=true
```

E o backend usa:

```text
issuer-uri: http://localhost:8080/realms/laboratorio-keycloak
jwk-set-uri: http://keycloak:8080/realms/laboratorio-keycloak/protocol/openid-connect/certs
policy-enforcer.auth-server-url: http://keycloak:8080
```

## Por Que Este Fluxo E Mais Realista?

Porque o frontend nao precisa conhecer detalhes de UMA, RPT, recurso ou escopo.

O frontend so sabe:

```text
estou logado e vou chamar a API
```

O backend sabe:

```text
esta rota exige documentos#ler
vou consultar o Keycloak
se autorizado, sigo para o Policy Enforcer
```

## Fontes Oficiais

- Spring Security Resource Server JWT: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
- Keycloak Policy Enforcer: https://www.keycloak.org/securing-apps/policy-enforcer
