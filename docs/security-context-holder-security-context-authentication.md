# SecurityContextHolder, SecurityContext e Authentication

Este guia explica como o Spring Security guarda os dados do usuario logado durante uma requisicao.

A frase central:

```text
SecurityContextHolder e o mecanismo global para acessar a ficha de seguranca da requisicao atual.
Cada requisicao tem sua propria ficha.
```

Observacao sobre visualizacao:

```text
Alguns previews do VS Code mostram blocos Mermaid como codigo puro.
Se isso acontecer, use os fluxos em texto logo abaixo de cada Mermaid.
```

## Mapa Mental

```text
SecurityContextHolder
  -> SecurityContext da requisicao atual
      -> Authentication
          -> usuario
          -> authorities/permissoes
          -> principal
          -> authenticated=true
```

## Os Dois Grandes Momentos

Para desfazer a bola de linhas, separe Spring Security em dois momentos:

```text
Momento 1: subida da aplicacao
  -> Spring monta a seguranca
  -> SegurancaConfig entra em cena
  -> SecurityFilterChain e criada
  -> filtros ficam prontos
```

```text
Momento 2: chegada de uma requisicao
  -> request passa pelos filtros
  -> token e lido
  -> JWT e validado
  -> Authentication e criada
  -> SecurityContext e preenchido
  -> controller executa ou e bloqueado
```

Uma forma curta:

```text
Na subida: o Spring monta a maquina.
Na requisicao: a maquina trabalha.
```

## Fluxo Da Subida Do Servico

Quando voce roda:

```bash
mvn -pl backend spring-boot:run
```

ou inicia a aplicacao pela IDE, acontece algo assim:

```mermaid
flowchart TD
    A["Spring Boot inicia"] --> B["Escaneia classes do projeto"]
    B --> C["Encontra @Configuration"]
    C --> D["Encontra SegurancaConfig"]
    D --> E["Le @EnableMethodSecurity"]
    E --> F["Habilita @PreAuthorize nos metodos"]
    F --> G["Executa metodo @Bean securityFilterChain"]
    G --> H["HttpSecurity monta regras"]
    H --> I["Cria SecurityFilterChain"]
    I --> J["Registra filtros no servidor web"]
    J --> K["Tomcat fica pronto na porta 8081"]
```

Versao em texto:

```text
Spring Boot inicia
  -> escaneia classes do projeto
  -> encontra @Configuration
  -> encontra SegurancaConfig
  -> le @EnableMethodSecurity
  -> habilita @PreAuthorize nos metodos
  -> executa o metodo @Bean securityFilterChain
  -> HttpSecurity monta regras
  -> cria SecurityFilterChain
  -> registra filtros no servidor web
  -> Tomcat fica pronto na porta 8081
```

Nesse momento ainda nao existe usuario logado.

Tambem ainda nao existe JWT de usuario sendo validado.

O que existe e a preparacao:

```text
Spring, quando uma request chegar, use estas regras.
```

## Onde A `SegurancaConfig` Entra Na Subida

Arquivo:

```text
backend/src/main/java/br/com/wanderlei/keycloakestudo/configuracao/SegurancaConfig.java
```

Classe:

```java
@Configuration
@EnableMethodSecurity
public class SegurancaConfig {
```

`@Configuration` significa:

```text
Spring, leia esta classe porque ela cria configuracoes/beans.
```

`@EnableMethodSecurity` significa:

```text
Spring, habilite seguranca em metodos, como @PreAuthorize.
```

Sem `@EnableMethodSecurity`, este tipo de anotacao nao seria aplicado corretamente:

```java
@PreAuthorize("hasAuthority('documentos:ler')")
```

## O Bean `SecurityFilterChain`

Na subida, o Spring chama este metodo:

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http,
                                        @Value("${laboratorio.keycloak.client-id}") String clientId) throws Exception {
```

Esse metodo devolve:

```java
SecurityFilterChain
```

Pensa nela como:

```text
uma esteira de filtros por onde toda requisicao HTTP vai passar.
```

O `HttpSecurity` e o construtor dessa esteira.

Ele configura:

```text
CSRF
rotas publicas
rotas autenticadas
Resource Server JWT
conversor de JWT
```

## Regras De Endpoint Na Subida

Este trecho:

```java
.authorizeHttpRequests(registry -> registry
        .requestMatchers("/publico/**").permitAll()
        .requestMatchers("/usuario/**").authenticated()
        .requestMatchers("/documentos/**").authenticated()
        .anyRequest().authenticated()
)
```

monta uma tabela mental:

| Padrao | Regra |
|---|---|
| `/publico/**` | pode passar sem login |
| `/usuario/**` | precisa estar autenticado |
| `/documentos/**` | precisa estar autenticado |
| qualquer outro | precisa estar autenticado |

Isso ainda nao olha `documentos:ler` ou `documentos:criar`.

Aqui e o primeiro portao:

```text
esta logado ou nao esta logado?
```

As permissoes especificas entram depois, com:

```java
@PreAuthorize("hasAuthority('documentos:criar')")
```

## Configuracao Do Resource Server JWT

Este trecho:

```java
.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(new ConversorPermissoesJwt(clientId)))
)
```

diz:

```text
Esta API e um Resource Server.
Ela recebe token JWT.
Ela precisa validar o JWT.
Depois de validar, use ConversorPermissoesJwt.
```

O `clientId` vem do `application.yml`:

```yaml
laboratorio:
  keycloak:
    client-id: laboratorio-api
```

Ele e usado no conversor para procurar as roles do client certo dentro do token:

```text
resource_access.laboratorio-api.roles
```

## O Que Sao Os Filtros Colocados Na Config?

Voce nao cria manualmente cada filtro.

Quando escreve:

```java
.oauth2ResourceServer(oauth2 -> oauth2.jwt(...))
```

o Spring Security adiciona filtros internos apropriados para Resource Server com JWT.

Um deles e o:

```text
BearerTokenAuthenticationFilter
```

Pensa assim:

```text
SegurancaConfig nao processa request diretamente.
SegurancaConfig monta a SecurityFilterChain.
SecurityFilterChain contem filtros.
Filtros processam cada request.
```

Fluxo de responsabilidade:

```text
SegurancaConfig
  -> define regras
  -> cria SecurityFilterChain
  -> Spring registra filtros
  -> filtros trabalham nas requests
```

## Fluxo Da Request Com Usuario Logado

Depois que a aplicacao esta no ar, vem a segunda parte.

Exemplo:

```http
GET /documentos
Authorization: Bearer eyJ...
```

Fluxo:

```mermaid
flowchart TD
    A["Cliente envia request com Bearer token"] --> B["Tomcat recebe request"]
    B --> C["Request entra na SecurityFilterChain"]
    C --> D["BearerTokenAuthenticationFilter procura Authorization"]
    D --> E["Extrai o token JWT"]
    E --> F["Resource Server valida JWT"]
    F --> G{"JWT valido?"}
    G -- "Nao" --> H["401 Unauthorized"]
    G -- "Sim" --> I["ConversorPermissoesJwt converte claims em authorities"]
    I --> J["Spring cria JwtAuthenticationToken"]
    J --> K["Authentication entra no SecurityContext"]
    K --> L["SecurityContext fica acessivel pelo SecurityContextHolder"]
    L --> M["@PreAuthorize verifica authorities"]
    M --> N{"Tem permissao?"}
    N -- "Nao" --> O["403 Forbidden"]
    N -- "Sim" --> P["Controller executa"]
```

Versao em texto:

```text
Cliente envia request com Bearer token
  -> Tomcat recebe request
  -> request entra na SecurityFilterChain
  -> BearerTokenAuthenticationFilter procura Authorization
  -> extrai o token JWT
  -> Resource Server valida JWT
      -> se invalido: 401 Unauthorized
      -> se valido:
          -> ConversorPermissoesJwt converte claims em authorities
          -> Spring cria JwtAuthenticationToken
          -> Authentication entra no SecurityContext
          -> SecurityContext fica acessivel pelo SecurityContextHolder
          -> @PreAuthorize verifica authorities
              -> se nao tem permissao: 403 Forbidden
              -> se tem permissao: Controller executa
```

## Como O JWT E Validado

O Spring usa esta configuracao:

```text
backend/src/main/resources/application.yml
```

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/laboratorio-keycloak
```

Com o `issuer-uri`, o Spring descobre:

```text
http://localhost:8080/realms/laboratorio-keycloak/.well-known/openid-configuration
```

Nesse endpoint, o Keycloak informa onde estao as chaves publicas:

```text
jwks_uri
```

Com essas chaves, o Spring valida:

```text
assinatura do token
issuer
expiracao
estrutura do JWT
```

Se alguem alterar o token na mao:

```text
assinatura nao bate -> 401
```

Se o token vier de outro realm:

```text
issuer nao bate -> 401
```

Se o token estiver expirado:

```text
expiracao falhou -> 401
```

## Quando O Usuario Vai Para O SecurityContext?

So depois que o JWT e validado.

Antes da validacao:

```text
nao confia no token
nao cria usuario autenticado
nao coloca Authentication no SecurityContext
```

Depois da validacao:

```text
JWT valido
  -> ConversorPermissoesJwt cria authorities
  -> JwtAuthenticationToken e criado
  -> Authentication entra no SecurityContext
```

Esse e o ponto em que o usuario fica disponivel para:

```text
@PreAuthorize
Authentication authentication no controller
@AuthenticationPrincipal Jwt jwt
SecurityContextHolder.getContext()
```

## Metafora Da Ficha

Pense assim:

```text
SecurityContextHolder = arquivo central de atendimento
SecurityContext       = ficha da requisicao atual
Authentication        = dados do usuario daquela ficha
```

Mesmo se o mesmo usuario fizer duas requisicoes:

```text
Request 1 do editor
  -> SecurityContext A
      -> Authentication A

Request 2 do editor
  -> SecurityContext B
      -> Authentication B
```

Os dados podem ser iguais, mas as fichas nao sao a mesma coisa.

## SecurityContextHolder E Global?

Sim, o acesso ao `SecurityContextHolder` e global/estatico.

Voce pode chamar de qualquer lugar:

```java
SecurityContextHolder.getContext()
```

Mas isto nao significa que existe um unico usuario global para todo mundo.

O mais importante:

```text
SecurityContextHolder e global.
O SecurityContext dentro dele nao e compartilhado globalmente entre todas as requisicoes.
```

Em aplicacoes web, o Spring normalmente usa `ThreadLocal`.

Visual:

```text
SecurityContextHolder global
  ├── Thread A -> SecurityContext do editor
  ├── Thread B -> SecurityContext do leitor
  └── Thread C -> SecurityContext vazio/anonimo
```

Cada requisicao enxerga o seu proprio contexto.

## Por Que Nao Pode Ser Um Contexto Global Unico?

Porque seria perigoso.

Imagine:

```text
Request do editor termina
Thread e reutilizada
Request anonima pega dados antigos do editor
```

Isso seria um vazamento de seguranca.

Por isso, no fim da requisicao, o Spring limpa o contexto.

Fluxo:

```text
request comeca
  -> Spring prepara contexto
  -> filtro autentica usuario
  -> Authentication entra no SecurityContext
  -> controller executa
  -> request termina
  -> Spring limpa SecurityContextHolder
```

## Ciclo De Vida Em API Com JWT

No nosso projeto, a API e stateless.

Isso quer dizer:

```text
A API nao guarda sessao para lembrar o usuario.
Cada requisicao precisa mandar o token de novo.
```

Fluxo:

```text
1. request chega
2. Spring le Authorization: Bearer <token>
3. Spring valida o JWT
4. Spring cria Authentication
5. Spring coloca Authentication no SecurityContext
6. controller usa os dados
7. request termina
8. Spring limpa o contexto
```

Mesmo usuario, nova requisicao:

```text
novo token lido
ou mesmo token lido de novo
JWT validado de novo
Authentication criada de novo
SecurityContext preenchido de novo
```

## O Que E Authentication?

`Authentication` e o objeto que representa o usuario autenticado.

No nosso caso, com JWT, a implementacao comum e:

```text
JwtAuthenticationToken
```

Dentro dele ficam dados como:

```text
name: editor
principal: Jwt
authorities:
  - documentos:ler
  - documentos:criar
  - documentos:editar
authenticated: true
```

## O Que E SecurityContext?

`SecurityContext` e a ficha de seguranca da requisicao.

Ele guarda a `Authentication`.

Conceitualmente:

```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication authentication = context.getAuthentication();
```

## O Que E SecurityContextHolder?

`SecurityContextHolder` e o ponto de acesso.

Ele sabe onde esta o `SecurityContext` da execucao atual.

Voce usa assim:

```java
Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
```

Leia assim:

```text
SecurityContextHolder:
  "me da o contexto de seguranca desta requisicao"

SecurityContext:
  "tenho aqui a autenticacao atual"

Authentication:
  "este e o usuario e estas sao as permissoes"
```

## Como O Usuario Vai Para O SecurityContext?

No nosso projeto, isso acontece por causa da `SecurityFilterChain`.

Arquivo:

```text
backend/src/main/java/br/com/wanderlei/keycloakestudo/configuracao/SegurancaConfig.java
```

Trecho importante:

```java
.oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt.jwtAuthenticationConverter(new ConversorPermissoesJwt(clientId)))
)
```

Esse trecho configura o Spring Security como Resource Server JWT.

Quando chega uma request com:

```http
Authorization: Bearer eyJ...
```

o Spring usa filtros internos para:

```text
1. ler o header Authorization
2. extrair o token Bearer
3. validar o JWT
4. converter o JWT em Authentication
5. colocar a Authentication no SecurityContext
```

## BearerTokenAuthenticationFilter

Esse filtro e uma das pecas internas do Spring Security para APIs com Bearer Token.

Ele trabalha com este header:

```http
Authorization: Bearer <token>
```

### 1. Olha O Header Authorization

O filtro verifica se a requisicao tem:

```text
Authorization
```

e se o valor comeca com:

```text
Bearer
```

Se nao tiver token e o endpoint exigir autenticacao:

```text
401 Unauthorized
```

Se o endpoint for publico:

```text
segue sem usuario autenticado
```

### 2. Extrai O Token

De:

```http
Authorization: Bearer eyJhbGciOiJSUzI1NiIs...
```

ele pega somente:

```text
eyJhbGciOiJSUzI1NiIs...
```

Esse valor e o JWT.

### 3. Pede Para O Resource Server Validar

A validacao usa a configuracao:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/laboratorio-keycloak
```

O Spring consulta o Keycloak para descobrir as chaves publicas e valida:

```text
assinatura
issuer
expiracao
estrutura do JWT
```

Se falhar:

```text
401 Unauthorized
```

### 4. Recebe Uma Authentication

Depois do JWT ser validado, o Spring chama o conversor configurado:

```java
new ConversorPermissoesJwt(clientId)
```

Esse conversor devolve:

```java
new JwtAuthenticationToken(jwt, authorities, nome)
```

Esse objeto e a `Authentication`.

### 5. Coloca No SecurityContext

Conceitualmente, o Spring faz algo como:

```java
SecurityContextHolder.getContext().setAuthentication(authentication);
```

Depois disso, durante aquela requisicao, o usuario esta disponivel no contexto de seguranca.

## O Que Sao Claims?

Claims sao informacoes dentro do JWT.

Um JWT tem tres partes:

```text
header.payload.signature
```

O `payload` guarda os claims.

Exemplo:

```json
{
  "sub": "123",
  "preferred_username": "editor",
  "email": "editor@laboratorio.local",
  "iss": "http://localhost:8080/realms/laboratorio-keycloak",
  "exp": 1760000000,
  "resource_access": {
    "laboratorio-api": {
      "roles": [
        "documentos:ler",
        "documentos:criar",
        "documentos:editar"
      ]
    }
  }
}
```

Cada campo e um claim:

```text
sub
preferred_username
email
iss
exp
resource_access
```

No Spring, lemos assim:

```java
jwt.getClaim("resource_access")
jwt.getClaimAsString("preferred_username")
jwt.getClaimAsStringList("scp")
```

## Claims Viram Authorities

O JWT vem com dados.

O Spring Security decide acesso usando authorities.

Por isso criamos:

```text
ConversorPermissoesJwt
```

Arquivo:

```text
backend/src/main/java/br/com/wanderlei/keycloakestudo/configuracao/ConversorPermissoesJwt.java
```

Fluxo:

```text
JWT do Keycloak
  -> claims
  -> ConversorPermissoesJwt
  -> SimpleGrantedAuthority
  -> Authentication
  -> SecurityContext
  -> @PreAuthorize
```

Frase para guardar:

```text
Claims sao dados dentro do token.
Authorities sao permissoes que o Spring usa para decidir acesso.
ConversorPermissoesJwt e a ponte entre os dois.
```

## Como Recuperar Dados Do Usuario

### Opção 1: Receber `Authentication` No Controller

```java
@GetMapping("/perfil")
public Map<String, Object> perfil(Authentication authentication) {
    return Map.of(
            "nome", authentication.getName(),
            "permissoes", authentication.getAuthorities()
    );
}
```

### Opção 2: Receber O `Jwt`

```java
@GetMapping("/perfil")
public Map<String, Object> perfil(@AuthenticationPrincipal Jwt jwt) {
    return Map.of(
            "usuario", jwt.getClaimAsString("preferred_username"),
            "email", jwt.getClaimAsString("email")
    );
}
```

### Opção 3: Usar `SecurityContextHolder`

```java
Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
```

Em controller, geralmente prefira receber por parametro. Fica mais claro e mais facil de testar.

## Fluxograma Completo

```mermaid
flowchart TD
    A["Request chega com Authorization Bearer"] --> B["SecurityFilterChain"]
    B --> C["BearerTokenAuthenticationFilter"]
    C --> D["Extrai token"]
    D --> E["Resource Server valida JWT"]
    E --> F{"JWT valido?"}
    F -- "Nao" --> G["401 Unauthorized"]
    F -- "Sim" --> H["ConversorPermissoesJwt"]
    H --> I["Claims viram authorities"]
    I --> J["JwtAuthenticationToken"]
    J --> K["SecurityContext"]
    K --> L["SecurityContextHolder"]
    L --> M["@PreAuthorize consulta Authentication"]
    M --> N{"Tem permissao?"}
    N -- "Nao" --> O["403 Forbidden"]
    N -- "Sim" --> P["Controller executa"]
```

Versao em texto:

```text
Request chega com Authorization Bearer
  -> SecurityFilterChain
  -> BearerTokenAuthenticationFilter
  -> extrai token
  -> Resource Server valida JWT
      -> se JWT invalido: 401 Unauthorized
      -> se JWT valido:
          -> ConversorPermissoesJwt
          -> claims viram authorities
          -> JwtAuthenticationToken
          -> SecurityContext
          -> SecurityContextHolder
          -> @PreAuthorize consulta Authentication
              -> se nao tem permissao: 403 Forbidden
              -> se tem permissao: Controller executa
```

## Resumo Final

```text
SecurityContextHolder
= acesso global/estatico ao contexto da requisicao atual
```

```text
SecurityContext
= ficha de seguranca da requisicao atual
```

```text
Authentication
= usuario autenticado e suas permissoes
```

```text
JWT
= token enviado pelo cliente com claims
```

```text
ConversorPermissoesJwt
= transforma claims em authorities
```

```text
@PreAuthorize
= consulta a Authentication que esta no SecurityContext
```
