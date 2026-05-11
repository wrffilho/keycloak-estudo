# Conceitos do Keycloak

Explicacao dos conceitos principais do Keycloak usados neste projeto: JWT, Realm, Client, Roles, Authorization Server, RPT e claims customizadas. Cada termo novo e explicado no momento em que aparece.

---

## JWT — o crachá digital

Quando voce faz login em qualquer sistema moderno, o servidor nao te da uma sessao guardada no banco. Ele te da um **JWT** (JSON Web Token) — um arquivo de texto codificado que voce carrega como um cracha.

Esse cracha tem tres partes separadas por ponto:

```
eyJhbGci...   .   eyJ1c2Vy...   .   SflKxwR...
   cabecalho         payload          assinatura
```

A parte do meio e so Base64 — voce pode decodificar no jwt.io agora mesmo e ver o conteudo. Parece assustador mas e literalmente um JSON:

```json
{
  "sub": "a3f2c1...",
  "preferred_username": "leitor",
  "email": "leitor@lab.com",
  "realm_access": {
    "roles": ["leitor"]
  },
  "exp": 1748000000
}
```

Cada campo desse JSON chama de **claim** (alegacao). O token "alega" que voce e o usuario `leitor`, que tem a role `leitor`, e que o token expira em tal data.

A **assinatura** no final garante que ninguem alterou o conteudo. O backend valida essa assinatura sem precisar perguntar pro Keycloak — e por isso que e rapido e sem estado.

---

## Realm — o universo isolado

No Keycloak, tudo vive dentro de um **Realm** (reino). Pense como um condominio: cada condominio tem seus proprios moradores, regras, apartamentos. Um usuario do condominio A nao existe no condominio B.

Num projeto real voce normalmente tem:
- `master` — o realm administrativo do proprio Keycloak (nao use pra suas apps)
- `laboratorio` — o seu realm, com seus usuarios e aplicacoes

Tudo que existe neste documento (usuarios, roles, clients, permissoes) existe **dentro de um realm**.

---

## Client — as aplicacoes cadastradas

Dentro do realm, voce cadastra cada aplicacao como um **Client** (cliente). Nao e "cliente" no sentido de pessoa, e "cliente do Keycloak" — uma aplicacao que vai usar o Keycloak para autenticacao ou autorizacao.

Neste projeto tem tres:

| Client | Papel |
|---|---|
| `laboratorio-frontend` | A SPA Vue que faz login do usuario |
| `laboratorio-api` | O backend Spring que valida os tokens |
| `laboratorio-menu` | Onde o catalogo de menus e permissoes fica |

Cada client tem configuracoes proprias: URL de redirect, tipo de fluxo de login, se tem autorizacao habilitada, etc.

---

## Roles — o que o usuario pode ser

**Role** (papel) e uma etiqueta que voce cola num usuario. E a forma mais simples de dizer "esse usuario e editor", "esse e aprovador".

Existem dois tipos:

**Realm Role** — vale para todo o realm, todas as aplicacoes dentro dele. Se voce da a role `admin` do realm pro usuario, todas as aplicacoes do realm enxergam ele como admin.

**Client Role** — vale so dentro de um client especifico. A role `editor` do client `laboratorio-menu` so existe no contexto desse client. Outro client nao ve essa role a menos que voce mapeie explicitamente.

No JWT do usuario voce ve os dois:

```json
{
  "realm_access": {
    "roles": ["offline_access", "uma_authorization"]
  },
  "resource_access": {
    "laboratorio-api": {
      "roles": ["leitor", "editor"]
    }
  }
}
```

Neste projeto as roles usadas pro menu ficam no client `laboratorio-menu` e as usadas pra proteger endpoints ficam no `laboratorio-api`.

---

## Authorization Server — quando roles nao bastam

Roles respondem "quem e o usuario". Mas as vezes a pergunta e mais fina: "o usuario `editor` pode **editar** o documento 42?"

Pra isso o Keycloak tem um modulo chamado **Authorization Server** — voce habilita isso dentro de um client especifico (no painel: Client → Authorization → Enable). Quando habilitado, esse client passa a ter quatro conceitos novos.

### Resource — o que voce quer proteger

**Resource** (recurso) e qualquer coisa que voce quer controlar o acesso. Pode ser uma URL, um documento, um botao, um item de menu.

Voce cria um resource e da atributos pra ele. No `laboratorio-api`, o resource e o proprio "Documento". No `laboratorio-menu`, cada item de menu e um resource com atributos customizados como `rotulo`, `rota` e `roleNecessaria`.

### Scope — o que pode ser feito com o resource

**Scope** (escopo) sao as acoes possiveis sobre um resource. Se o resource e "Documento", os scopes sao: `ler`, `criar`, `editar`, `aprovar`.

Voce nao precisa de scope pra tudo. Em resources simples (como itens de menu que ou aparecem ou nao) nao faz sentido ter scope.

### Policy — a regra

**Policy** (politica) e uma regra que decide se o acesso e permitido. A regra pode ser baseada em:

- **Role**: "permitido se o usuario tem a role `editor`"
- **Usuario**: "permitido so pro usuario `wanderlei`"
- **Tempo**: "permitido so em horario comercial"
- **JavaScript**: logica customizada

A policy em si nao sabe o que esta protegendo. Ela so diz: dado esse contexto, e sim ou nao.

### Permission — a combinacao

**Permission** (permissao) junta resource + scope + policy. E ela que diz: "para acessar o resource `Documento` com o scope `editar`, aplique a policy `apenas-editores`".

```
Permission: "editores-podem-editar-documentos"
  Resource: Documento
  Scope: editar
  Policy: apenas-editores (que verifica role "editor")
```

O fluxo de decisao e:

```
Usuario quer fazer algo
  → Keycloak olha: qual resource? qual scope?
  → Encontra a Permission que cobre isso
  → Avalia a Policy dessa Permission
  → Sim ou Nao
```

---

## RPT — o token de autorizacao

O JWT comum que o usuario recebe no login e chamado de **Access Token**. Ele diz quem o usuario e e quais roles ele tem.

O **RPT** (Requesting Party Token) e um segundo token, mais rico. O Keycloak avalia as policies e devolve um token novo que **contem as permissoes aprovadas** embutidas:

```json
{
  "authorization": {
    "permissions": [
      {
        "rsname": "Documento",
        "scopes": ["ler", "editar"]
      }
    ]
  }
}
```

No Access Token comum voce ve roles. No RPT voce ve **permissoes concretas ja avaliadas** pelo motor de autorizacao do Keycloak.

Quando usar qual:

| | Access Token (JWT comum) | RPT |
|---|---|---|
| Conteudo | Roles, dados do usuario | Permissoes concretas avaliadas |
| Quando e emitido | No login | Quando voce pede explicitamente |
| Pra que serve | Autenticar quem e o usuario | Saber exatamente o que pode fazer |
| Custo | Barato (ja existe) | Mais caro (nova chamada pro Keycloak) |

Neste projeto o backend le o campo `authorization.permissions` do token via `ConversorPermissoesJwt.java` e converte em authorities do Spring como `documentos:ler` e `documentos:criar`.

---

## Adicionar campos no JWT

Sim, da pra adicionar qualquer campo que quiser no token. Isso se chama **Claim Mapper** (mapeador de claims).

Voce configura dentro do client (ou do realm inteiro) quais informacoes extras devem ser incluidas no token. Exemplos comuns:

- Departamento do usuario (vindo de um atributo do usuario no Keycloak)
- Email
- Grupos do usuario
- Qualquer atributo custom que voce cadastrou no perfil do usuario

No Keycloak: Client → Client Scopes → adicionar um mapper.

Voce pode mapear:
- **Atributo do usuario** → claim no token
- **Role** → claim no token
- **Dado fixo** → claim no token
- **Script** → logica customizada pra montar o claim

O backend recebe tudo isso dentro do JWT e pode usar normalmente. No projeto, o `ConversorPermissoesJwt.java` le claims especificas do token — se voce adicionar uma nova claim, basta le-la la tambem.

---

## O mapa do projeto

```
Realm (laboratorio)
  ├─ Usuarios
  │    └─ tem Client Roles atribuidas por client
  │
  └─ Clients
       │
       ├─ laboratorio-frontend
       │    └─ SPA Vue que faz login
       │         └─ JWT emitido ja inclui authorization.permissions de laboratorio-api
       │
       ├─ laboratorio-api   ← tem Authorization Server completo
       │    ├─ Client Roles  → leitor, editor, aprovador...
       │    ├─ Resources     → Documento
       │    ├─ Scopes        → ler, criar, editar, aprovar
       │    ├─ Policies      → baseadas em roles (ex: "editor pode criar")
       │    └─ Permissions   → juntam Resource + Scope + Policy
       │         │
       │         └─ Keycloak avalia e injeta no JWT:
       │              "authorization": { "permissions": [
       │                { "rsname": "Documento", "scopes": ["ler", "criar"] }
       │              ]}
       │                   │
       │                   └─ ConversorPermissoesJwt.java le isso
       │                        └─ vira authorities: "documentos:ler", "documentos:criar"
       │                             └─ /usuario/permissoes devolve ao frontend
       │                                  └─ frontend usa pra habilitar/desabilitar botoes
       │
       └─ laboratorio-menu   ← usa Keycloak como catalogo, NAO como motor de decisao
            ├─ Client Roles  → leitor, editor, aprovador, gestor, administrador
            └─ Resources (type=frontend-menu)
                 └─ cada resource = item de menu com atributos:
                      rotulo, rota, ordem, pai, roleNecessaria
                           │
                           └─ Java le os resources + roles do usuario via Admin API
                                └─ Java decide: mostra se roles.contains(item.roleNecessaria)
```

---

## A diferenca entre os dois clients

| | `laboratorio-api` | `laboratorio-menu` |
|---|---|---|
| Tem policies e permissions? | Sim | Nao |
| Quem toma a decisao? | Keycloak (avalia as policies) | Java (filtra no codigo) |
| Resources servem pra que? | O que esta sendo protegido | Catalogo de dados (itens de menu) |
| Resultado vai onde? | Dentro do JWT (`authorization.permissions`) | Atributos lidos via Admin API |

O `laboratorio-api` usa o **motor de autorizacao do Keycloak**: voce configura as regras la dentro, o Keycloak avalia e devolve o resultado embutido no token. O `laboratorio-menu` usa o Keycloak apenas como **armazenamento de configuracao**: os dados ficam la, mas a logica de decisao esta no Java.
