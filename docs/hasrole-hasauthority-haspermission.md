# `hasRole`, `hasAuthority` e `hasPermission`

Este guia explica tres formas comuns de proteger metodos no Spring Security.

No nosso projeto, elas respondem perguntas parecidas, mas em niveis diferentes:

```text
hasRole       -> quem voce e?
hasAuthority  -> o que voce pode fazer?
hasPermission -> o que voce pode fazer neste objeto especifico?
```

## Mapa Rapido

| Expressao | Pergunta mental | Exemplo |
|---|---|---|
| `hasRole('ADMIN')` | Voce e admin? | area administrativa |
| `hasAuthority('documentos:editar')` | Voce pode editar documentos? | editar qualquer documento |
| `hasPermission(#id, 'Documento', 'editar')` | Voce pode editar este documento? | editar somente documento que voce criou |

## `hasRole`

`hasRole` verifica papel.

Papel tambem aparece como `role`.

Exemplos de papeis:

```text
ADMIN
EDITOR
APROVADOR
LEITOR
```

Exemplo em codigo:

```java
@PreAuthorize("hasRole('ADMIN')")
```

Leitura:

```text
So entra se o usuario tiver papel ADMIN.
```

Detalhe importante:

```java
hasRole('ADMIN')
```

normalmente procura internamente:

```text
ROLE_ADMIN
```

Entao isto:

```java
@PreAuthorize("hasRole('ADMIN')")
```

e parecido com isto:

```java
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
```

Uso bom:

```text
area administrativa
painel interno
funcao ampla do sistema
```

Exemplo mental:

```text
Voce e administrador?
```

## `hasAuthority`

`hasAuthority` verifica uma permissao exata.

No nosso projeto usamos:

```java
@PreAuthorize("hasAuthority('documentos:ler')")
```

ou:

```java
@PreAuthorize("hasAuthority('documentos:criar')")
```

Leitura:

```text
So entra se o usuario tiver essa permissao.
```

Exemplos de authorities/permissoes:

```text
documentos:ler
documentos:criar
documentos:editar
documentos:aprovar
```

Uso bom:

```text
listar documentos
criar documentos
editar documentos em geral
aprovar documentos em geral
exportar backup do Keycloak
```

Exemplo mental:

```text
Voce pode editar documentos?
```

## `hasPermission`

`hasPermission` verifica permissao sobre um objeto especifico.

Exemplo:

```java
@PreAuthorize("hasPermission(#id, 'Documento', 'editar')")
```

Leitura:

```text
So entra se o usuario puder editar este documento especifico.
```

Aqui `#id` e o id que veio na URL:

```java
@PutMapping("/{id}")
```

Se a URL for:

```http
PUT /documentos/doc-001
```

entao:

```text
#id = doc-001
```

Uso bom:

```text
editar somente documento criado pelo usuario
aprovar somente documento de outro usuario
ver pedido somente da propria empresa
baixar arquivo somente se participa do projeto
```

Exemplo mental:

```text
Voce pode editar este documento aqui?
```

## Comparacao Com Documento

Imagine:

```text
doc-001 criado por editor
doc-002 criado por joao
```

Usuario logado:

```text
editor
```

### Usando `hasRole`

```java
@PreAuthorize("hasRole('EDITOR')")
```

Pergunta:

```text
O usuario e editor?
```

Resultado:

```text
editor editando doc-001 -> pode
editor editando doc-002 -> pode
```

Motivo:

```text
So olhou o papel EDITOR.
Nao olhou permissao especifica.
Nao olhou qual documento era.
```

### Usando `hasAuthority`

```java
@PreAuthorize("hasAuthority('documentos:editar')")
```

Pergunta:

```text
O usuario pode editar documentos?
```

Resultado:

```text
editor editando doc-001 -> pode
editor editando doc-002 -> pode
```

Motivo:

```text
Olhou permissao geral.
Nao olhou se o documento pertence ao usuario.
```

### Usando `hasPermission`

```java
@PreAuthorize("hasPermission(#id, 'Documento', 'editar')")
```

Pergunta:

```text
O usuario pode editar este documento especifico?
```

Resultado possivel:

```text
editor editando doc-001 -> pode
editor editando doc-002 -> nao pode
```

Motivo:

```text
Agora a regra pode olhar o documento, o dono, o status e outras regras de negocio.
```

## Tabela De Decisao

| Situacao | Melhor opcao |
|---|---|
| Liberar uma area inteira para admin | `hasRole` |
| Exigir uma acao especifica geral | `hasAuthority` |
| Exigir regra por objeto | `hasPermission` |
| Endpoint `GET /documentos` | `hasAuthority('documentos:ler')` |
| Endpoint `POST /documentos` | `hasAuthority('documentos:criar')` |
| Endpoint `PUT /documentos/{id}` com dono/status | `hasPermission(#id, 'Documento', 'editar')` |

## Como Isso Se Conecta Ao JWT

O JWT nao vem com `hasRole`, `hasAuthority` ou `hasPermission`.

O JWT vem com dados, por exemplo:

```json
{
  "preferred_username": "editor",
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

No nosso projeto, a classe `ConversorPermissoesJwt` le esses dados e transforma em authorities do Spring.

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

## Onde Cada Um Consulta

```text
hasRole
  consulta authorities com prefixo ROLE_
```

```text
hasAuthority
  consulta authorities exatamente como foram registradas
```

```text
hasPermission
  chama um PermissionEvaluator configurado por voce
```

## Resumo Final

```text
hasRole
= pergunta sobre identidade funcional
= "voce e admin?"
```

```text
hasAuthority
= pergunta sobre capacidade geral
= "voce pode criar documento?"
```

```text
hasPermission
= pergunta sobre capacidade contextual
= "voce pode editar este documento?"
```

No nosso laboratorio, `hasAuthority` e o melhor ponto de partida porque deixa claro o contrato entre Keycloak e API:

```text
Keycloak emite documentos:criar
API exige documentos:criar
```

Depois, quando quisermos regras por documento, `hasPermission` vira a proxima peca natural do estudo.

