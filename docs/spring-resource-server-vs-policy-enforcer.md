# Spring Resource Server vs Policy Enforcer

Este laboratorio possui branches separadas para comparar duas formas de proteger a mesma API:

- **Spring Resource Server**: o Spring valida o JWT e o proprio codigo da API decide se o usuario tem a permissao necessaria.
- **Keycloak Policy Enforcer**: o Spring continua validando o JWT, mas a decisao fina de autorizacao dos endpoints de documentos e delegada ao Keycloak Authorization Services.

As duas abordagens usam bearer token. A diferenca principal nao esta no login, nem na emissao do token. A diferenca esta em **quem toma a decisao final de autorizacao**.

## Resumo Da Diferenca

| Pergunta | Spring Resource Server | Policy Enforcer |
|---|---|---|
| Quem valida o JWT? | Spring Security | Spring Security continua validando o JWT nesta branch |
| Quem decide se pode acessar `/documentos`? | Codigo Java da API, via `@PreAuthorize` | Keycloak, via recurso, path, metodo, escopo, politica e permissao |
| Onde a regra aparece no backend? | No controller ou service, em anotacoes como `@PreAuthorize` | Na configuracao do `ServletPolicyEnforcerFilter` |
| Onde a regra de negocio de acesso fica centralizada? | Mais perto do codigo da API | Mais perto do Keycloak Authorization Services |
| O endpoint conhece a permissao exigida? | Sim, a anotacao mostra isso diretamente | Nao necessariamente; o endpoint recebe a requisicao depois do filtro autorizar |
| O que e mais didatico primeiro? | Ver token virar authority e `@PreAuthorize` bloquear | Ver Keycloak decidir por recurso e escopo |

## Conceitos Necessarios

### Bearer Token

O frontend ou Postman envia o access token no header:

```http
Authorization: Bearer eyJ...
```

Sem esse header, a API nao tem um usuario autenticado para avaliar.

### JWT

O JWT e o token emitido pelo Keycloak. Ele carrega dados como:

- `iss`: quem emitiu o token;
- `sub`: identificador do usuario;
- `preferred_username`: nome do usuario;
- `resource_access`: roles por client;
- `authorization.permissions`: permissoes emitidas pelo Authorization Services, quando usadas.

### RPT

RPT significa Requesting Party Token.

No fluxo com Policy Enforcer, o access token comum prova quem e o usuario, mas pode nao carregar a permissao fina para um recurso especifico.

Para chamar um endpoint protegido pelo Policy Enforcer, o cliente pode precisar trocar o access token comum por um RPT contendo a permissao pedida, por exemplo:

```text
recurso: documentos
escopo: ler
```

Quando o RPT e emitido, ele continua sendo um bearer token, mas passa a carregar a decisao de autorizacao em `authorization.permissions`.

### Autenticacao

Autenticacao responde:

> Quem e este usuario?

No projeto, essa parte continua sendo feita pelo Spring Security Resource Server.

### Autorizacao

Autorizacao responde:

> Este usuario pode executar esta acao neste recurso?

No Resource Server puro, essa pergunta e respondida pelo Spring usando authorities. No Policy Enforcer, essa pergunta e enviada ao Keycloak.

## Fluxo Com Spring Resource Server

Na branch baseada em Spring Resource Server, o caminho mental e:

```text
Postman ou frontend
  -> envia Authorization: Bearer <jwt>
  -> Spring Security recebe a requisicao
  -> Resource Server valida o JWT
  -> ConversorPermissoesJwt transforma claims em authorities
  -> @PreAuthorize compara authority exigida
  -> controller executa ou Spring retorna 403
```

### Passo 1: A Requisicao Chega

Exemplo:

```http
POST /documentos
Authorization: Bearer <token-do-editor>
Content-Type: application/json
```

O Spring Security intercepta antes do controller.

### Passo 2: O Spring Procura O Bearer Token

O Resource Server procura o token no header `Authorization`.

Se nao houver token, o fluxo para aqui:

```text
sem bearer token -> 401 Unauthorized
```

`401` significa: a API nao reconheceu um usuario autenticado.

### Passo 3: O Spring Valida O JWT

Com `spring.security.oauth2.resourceserver.jwt.issuer-uri`, o Spring descobre os metadados do realm e as chaves publicas do Keycloak.

Depois valida:

- assinatura do token;
- issuer;
- expiracao;
- formato esperado do JWT.

Se o token for invalido ou expirado:

```text
token invalido -> 401 Unauthorized
```

### Passo 4: O Projeto Converte Claims Em Authorities

Depois de aceitar o JWT, o Spring chama `ConversorPermissoesJwt`.

Esse conversor le dados do token e cria authorities como:

```text
documentos:ler
SCOPE_documentos:ler
documentos:criar
SCOPE_documentos:criar
```

Essas authorities sao o formato que o Spring Security usa para decidir acesso.

### Passo 5: O `@PreAuthorize` Decide

No Resource Server puro, o controller mostra a regra diretamente:

```java
@PostMapping
@PreAuthorize("hasAuthority('documentos:criar')")
public ResponseEntity<DocumentoResposta> criar(...) {
    ...
}
```

O significado e:

> Spring, so deixe executar este metodo se o usuario tiver a authority `documentos:criar`.

### Passo 6: Resultado

Para `POST /documentos`:

| Situacao | Resultado |
|---|---|
| Sem token | `401 Unauthorized` |
| Token invalido | `401 Unauthorized` |
| Token valido, sem `documentos:criar` | `403 Forbidden` |
| Token valido, com `documentos:criar` | `201 Created` |

## Fluxo Com Policy Enforcer

Na branch `feature/policy-enforcer-keycloak`, o caminho mental muda:

```text
Postman ou frontend
  -> obtem access token comum no Keycloak
  -> pede ao Keycloak um RPT para o recurso/escopo desejado
  -> envia Authorization: Bearer <rpt>
  -> Spring Security recebe a requisicao
  -> Resource Server valida o RPT como JWT
  -> Spring confirma que /documentos exige usuario autenticado
  -> ServletPolicyEnforcerFilter intercepta /documentos
  -> Policy Enforcer identifica path e metodo HTTP
  -> Policy Enforcer verifica se o RPT possui a permissao necessaria
  -> filtro libera a requisicao ou retorna 403
  -> controller executa sem @PreAuthorize
```

### Passo 1: A Requisicao Chega

Exemplo:

```http
POST /documentos
Authorization: Bearer <token-do-editor>
Content-Type: application/json
```

A requisicao ainda entra na cadeia de filtros do Spring.

### Passo 2: O Spring Ainda Autentica

Nesta branch, o Policy Enforcer nao substitui a validacao JWT do Spring.

O Spring ainda usa:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/laboratorio-keycloak
```

Isso e importante para o aprendizado: a autenticacao continua separada da autorizacao fina.

No fluxo do Policy Enforcer, o token enviado ao backend deve ser um RPT quando o endpoint exige permissao fina. O RPT tambem e um JWT, entao o Spring consegue valida-lo como bearer token.

### Passo 3: O Spring Exige Usuario Autenticado

Em `SegurancaConfig`, `/documentos/**` continua exigindo login:

```java
.requestMatchers("/documentos/**").authenticated()
```

Isso responde apenas:

> Existe um usuario autenticado?

Ainda nao responde:

> Ele pode criar, editar, ler ou aprovar documentos?

### Passo 4: O Policy Enforcer Intercepta Documentos

O filtro foi registrado apenas para:

```text
/documentos
/documentos/*
```

Isso evita que o Policy Enforcer interfira em endpoints publicos ou nos endpoints de usuario/menu.

### Passo 5: O Filtro Mapeia Path E Metodo Para Escopo

A configuracao da branch define este mapa:

| Path | Metodo | Recurso Keycloak | Escopo exigido |
|---|---|---|---|
| `/documentos` | GET | `documentos` | `ler` |
| `/documentos` | POST | `documentos` | `criar` |
| `/documentos/{id}` | GET | `documentos` | `ler` |
| `/documentos/{id}` | PUT | `documentos` | `editar` |
| `/documentos/{id}/aprovar` | POST | `documentos` | `aprovar` |

No codigo, isso aparece em `PolicyEnforcerConfig`:

```java
caminho("/documentos", metodo("GET", "ler"), metodo("POST", "criar"))
caminho("/documentos/{id}", metodo("GET", "ler"), metodo("PUT", "editar"))
caminho("/documentos/{id}/aprovar", metodo("POST", "aprovar"))
```

### Passo 6: O Cliente Pede Um RPT Ao Keycloak

Antes de chamar `/documentos`, o cliente pode pedir ao Keycloak um RPT para a permissao desejada.

Exemplo conceitual para leitura de documentos:

```text
grant_type=urn:ietf:params:oauth:grant-type:uma-ticket
audience=laboratorio-api
permission=documentos#ler
Authorization: Bearer <access-token-comum>
```

Se o usuario puder acessar o recurso `documentos` com escopo `ler`, o Keycloak responde com um novo `access_token`. Esse token e o RPT.

Se o usuario nao puder acessar, o Keycloak responde:

```json
{
  "error": "access_denied",
  "error_description": "not_authorized"
}
```

### Passo 7: O Keycloak Avalia A Politica

No realm importado, o client `laboratorio-api` possui Authorization Services habilitado.

Existe um recurso:

```text
documentos
```

Com escopos:

```text
ler
criar
editar
aprovar
```

Cada escopo tem uma permissao associada a uma politica por role:

| Escopo | Politica | Role exigida |
|---|---|---|
| `ler` | `Permissao para ler documentos` | `documentos:ler` |
| `criar` | `Permissao para criar documentos` | `documentos:criar` |
| `editar` | `Permissao para editar documentos` | `documentos:editar` |
| `aprovar` | `Permissao para aprovar documentos` | `documentos:aprovar` |

Para `POST /documentos`, o filtro pede uma decisao para:

```text
recurso: documentos
escopo: criar
```

O Keycloak verifica se o usuario possui a role exigida pela politica aplicada ao escopo `criar`.

### Passo 8: Resultado

Para `POST /documentos`:

| Situacao | Resultado |
|---|---|
| Sem token | `401 Unauthorized`, bloqueado antes da autorizacao fina |
| Token invalido | `401 Unauthorized`, bloqueado na autenticacao |
| Access token comum, sem permissao `authorization.permissions` para o recurso/escopo | `403 Forbidden`, bloqueado pelo Policy Enforcer |
| Pedido de RPT negado pelo Keycloak | `403 Forbidden` no token endpoint, com `not_authorized` |
| RPT valido, com escopo `criar` para `documentos` | `201 Created` |

## Comparacao Visual Dos Dois Fluxos

### Resource Server

```text
Token
  -> Spring valida JWT
  -> Conversor cria authorities
  -> @PreAuthorize exige authority
  -> metodo executa
```

### Policy Enforcer

```text
Access token comum
  -> cliente pede RPT ao Keycloak
  -> Spring valida RPT como JWT
  -> Policy Enforcer identifica path/metodo
  -> Policy Enforcer verifica permissao no RPT
  -> metodo executa
```

## Onde Fica A Regra De Acesso?

### No Resource Server

A regra fica no codigo:

```java
@PreAuthorize("hasAuthority('documentos:aprovar')")
```

Vantagens:

- facil de enxergar no controller;
- facil de testar com `MockMvc` e `jwt()`;
- usa apenas Spring Security;
- bom para aprender como claims viram authorities.

Custos:

- mudar regra pode exigir alterar codigo;
- a API precisa conhecer cada permissao;
- parte da modelagem feita no Keycloak pode virar apenas documentacao, se o token ja carregar tudo pronto.

### No Policy Enforcer

A regra fica na relacao entre:

- path da API;
- metodo HTTP;
- recurso do Keycloak;
- escopo do Keycloak;
- politica;
- permissao.

Vantagens:

- deixa o Keycloak mais responsavel pela autorizacao fina;
- aproxima o backend do modelo de Authorization Services;
- permite estudar recurso, escopo, politica e permissao de forma mais concreta;
- reduz anotacoes de autorizacao nos controllers protegidos pelo enforcer.

Custos:

- adiciona uma integracao especifica do Keycloak;
- exige configuracao cuidadosa de paths;
- erro de path pode liberar ou bloquear coisa errada;
- testes automatizados locais geralmente precisam desabilitar o filtro ou simular Keycloak.

## Como Interpretar `401` E `403`

`401 Unauthorized` nao significa "sem permissao". Significa que a API nao conseguiu autenticar a chamada.

Exemplos:

- nao enviou bearer token;
- token expirou;
- token foi emitido por outro issuer;
- assinatura nao bate com as chaves do realm.

`403 Forbidden` significa que a API reconheceu um usuario, mas a autorizacao negou a acao.

No Resource Server:

```text
403 = faltou authority esperada pelo @PreAuthorize
```

No Policy Enforcer:

```text
403 = RPT ausente, RPT sem a permissao exigida, ou Keycloak negou o pedido de RPT
```

## Exemplo Completo: Usuario `leitor` Tentando Criar Documento

### Com Resource Server

```text
1. leitor faz POST /documentos
2. Spring valida o JWT
3. ConversorPermissoesJwt encontra documentos:ler
4. Controller exige documentos:criar
5. Spring compara authorities
6. documentos:criar nao existe
7. resposta: 403 Forbidden
```

### Com Policy Enforcer

```text
1. leitor faz POST /documentos
2. cliente pede RPT para documentos#criar
3. Keycloak avalia a permissao para criar documentos
4. politica exige role documentos:criar
5. leitor tem apenas documentos:ler
6. Keycloak nega o RPT com not_authorized
7. se chamar a API sem RPT valido, resposta: 403 Forbidden
```

O resultado HTTP e igual. A diferenca e o lugar onde a decisao foi tomada.

## Exemplo Completo: Usuario `editor` Criando Documento

### Com Resource Server

```text
1. editor faz POST /documentos
2. Spring valida o JWT
3. ConversorPermissoesJwt encontra documentos:criar
4. Controller exige documentos:criar
5. Spring libera o metodo
6. documento e criado
7. resposta: 201 Created
```

### Com Policy Enforcer

```text
1. editor faz POST /documentos
2. cliente pede RPT para documentos#criar
3. Keycloak avalia a permissao para criar documentos
4. politica exige role documentos:criar
5. editor possui documentos:criar
6. Keycloak emite um RPT com a permissao
7. cliente chama POST /documentos usando Authorization: Bearer <rpt>
8. Spring valida o RPT como JWT
9. Policy Enforcer ve POST + /documentos
10. mapa exige recurso documentos + escopo criar
11. filtro encontra a permissao no RPT e libera a requisicao
12. controller cria o documento
13. resposta: 201 Created
```

## Por Que Esta Branch Ainda Usa Resource Server?

Porque Policy Enforcer e autorizacao. Ele nao elimina a necessidade de autenticar a chamada.

Neste laboratorio, a separacao fica assim:

| Camada | Responsabilidade |
|---|---|
| Spring Resource Server | Validar bearer token e montar usuario autenticado |
| ConversorPermissoesJwt | Continuar expondo permissoes para telas, diagnostico e endpoints que ainda usam authorities |
| Policy Enforcer | Exigir que o bearer token tenha a permissao fina dos endpoints `/documentos` |
| Keycloak Authorization Services | Manter recursos, escopos, politicas e permissoes |

## Quando Usar Cada Abordagem

Use Spring Resource Server puro quando:

- voce quer menos dependencias especificas do Keycloak;
- as regras sao simples;
- a equipe prefere ver a permissao exigida no codigo;
- os testes automatizados precisam ser simples;
- o token ja carrega tudo que a API precisa para decidir.

Use Policy Enforcer quando:

- voce quer estudar ou usar Keycloak Authorization Services de forma mais completa;
- o dominio usa recursos e escopos explicitamente;
- voce quer centralizar mais decisoes no Keycloak;
- regras de acesso podem mudar sem alterar tanto o codigo da API;
- voce aceita a complexidade extra da integracao.

## Cuidados Com Policy Enforcer

O Policy Enforcer depende muito do mapeamento correto entre API e Keycloak.

Revise sempre:

- se o path configurado bate com o path real do controller;
- se o metodo HTTP esta mapeado para o escopo correto;
- se o recurso existe no client `laboratorio-api`;
- se a permissao liga o recurso e o escopo corretos;
- se a politica exige a role esperada;
- se a politica de role referencia o ID correto da role, nao apenas um texto solto parecido com o nome;
- se o usuario de teste possui ou nao essa role.

Um erro comum e pensar apenas na role. No Policy Enforcer, a role normalmente e so uma parte da politica. A decisao completa passa por:

```text
path -> metodo -> recurso -> escopo -> permissao -> politica -> role do usuario
```

## Erro Comum: `ProtectedResource.find` Com `403 Forbidden`

Se o backend mostrar uma excecao parecida com esta:

```text
org.keycloak.authorization.client.util.HttpResponseException:
Unexpected response from server: 403 / Forbidden
...
org.keycloak.authorization.client.resource.ProtectedResource.find
```

o problema normalmente nao esta no usuario final `leitor`, `editor` ou `aprovador`.

Esse erro acontece antes da decisao de acesso do usuario. Ele ocorre quando o proprio backend, usando o client confidencial `laboratorio-api`, tenta consultar a Protection API do Keycloak para descobrir os recursos protegidos.

Para essa consulta funcionar, o client `laboratorio-api` precisa:

- ser confidencial;
- ter `serviceAccountsEnabled: true`;
- possuir secret valido;
- ter Authorization Services habilitado;
- ter a role `uma_protection` associada ao seu service account.

No laboratorio, isso fica assim:

```text
client: laboratorio-api
service account: service-account-laboratorio-api
role necessaria: laboratorio-api/uma_protection
```

Sem essa permissao, o enforcer nao consegue consultar `resource_set` na Protection API e o Keycloak retorna `403` para o backend.

Esse erro e diferente de um `403` normal retornado para o usuario final. Aqui o proprio backend falha ao conversar com o Keycloak.

## Erro Comum: Access Token Comum Retorna `403`

Depois que a Protection API esta correta, ainda e possivel receber `403` ao chamar `/documentos` com um access token comum.

Isso acontece porque o Policy Enforcer espera que o bearer token carregue a permissao do recurso protegido.

Para o fluxo do laboratorio:

```text
1. obter access token comum do usuario
2. pedir RPT para documentos#ler, documentos#criar, documentos#editar ou documentos#aprovar
3. chamar a API usando Authorization: Bearer <rpt>
```

Se o usuario `editor` pedir `documentos#ler`, o Keycloak deve responder `200` no token endpoint e emitir um RPT.

Se o usuario `leitor` pedir `documentos#criar`, o Keycloak deve responder `403` com `not_authorized`.

No frontend do laboratorio, o servico de documentos faz essa troca antes de chamar a API:

| Acao no frontend | Permissao pedida ao Keycloak | Endpoint chamado com RPT |
|---|---|---|
| Listar documentos | `documentos#ler` | `GET /documentos` |
| Abrir documento | `documentos#ler` | `GET /documentos/{id}` |
| Criar documento | `documentos#criar` | `POST /documentos` |
| Editar documento | `documentos#editar` | `PUT /documentos/{id}` |
| Aprovar documento | `documentos#aprovar` | `POST /documentos/{id}/aprovar` |

Por isso, um usuario `leitor` deve conseguir abrir a tela de documentos e listar documentos, mas nao deve conseguir criar, editar ou aprovar.

## Fontes Oficiais

- Spring Security Resource Server JWT: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
- Keycloak Policy Enforcer: https://www.keycloak.org/securing-apps/policy-enforcer
