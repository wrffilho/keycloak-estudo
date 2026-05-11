# Reconstruindo o Keycloak do zero

Guia passo a passo para recriar toda a configuracao do Keycloak deste laboratorio manualmente, sem usar o arquivo de importacao. Util para entender o que cada configuracao faz e por que existe.

> O caminho rapido e usar `docker compose up` — o arquivo `infra/keycloak/laboratorio-keycloak-realm.json` e importado automaticamente. Este guia e para quem quer entender o que aquele arquivo representa.

---

## 1. Subindo o Keycloak com Docker

Crie um `docker-compose.yml` minimo para comecar do zero:

```yaml
services:
  keycloak:
    image: quay.io/keycloak/keycloak:26.0.7
    container_name: keycloak-estudo
    command: start-dev
    environment:
      KC_BOOTSTRAP_ADMIN_USERNAME: admin
      KC_BOOTSTRAP_ADMIN_PASSWORD: admin
    ports:
      - "8080:8080"
```

Suba:

```bash
docker compose up -d
```

Acesse o painel administrativo em `http://localhost:8080` e entre com `admin` / `admin`.

---

## 2. Criando o Realm

**Realm** e o universo isolado onde tudo vai existir. Usuarios, clients, roles — tudo pertence a um realm.

No painel:
1. Clique no seletor de realm no canto superior esquerdo (mostra "Keycloak" por padrao)
2. Clique em **Create realm**
3. Preencha:
   - **Realm name**: `laboratorio-keycloak`
   - **Display name**: `Laboratorio Keycloak`
4. Clique em **Create**

Configuracoes extras do realm (aba **Realm settings**):
- **Login** → desabilite "User registration" (nao queremos que qualquer pessoa se cadastre)
- **Localization** → habilite internacionalizacao, adicione `pt-BR`, defina como padrao

---

## 3. Client `laboratorio-api`

Este client representa o backend Spring. Ele tem **Authorization Server** completo: resources, scopes, policies e permissions para controlar acesso aos documentos.

### 3.1 Criando o client

**Clients** → **Create client**

- **Client type**: OpenID Connect
- **Client ID**: `laboratorio-api`
- **Name**: API do Laboratorio

Proxima tela:
- **Client authentication**: ON (client confidencial, tem secret)
- **Authorization**: ON (habilita o Authorization Server)
- **Authentication flow**: marque apenas Standard flow e Direct access grants

Proxima tela:
- **Valid redirect URIs**: `*`
- **Web origins**: `*`

Salve.

### 3.2 Pegando o secret

Aba **Credentials** → copie o **Client secret**. No projeto o valor usado e `laboratorio-api-secret`. Voce pode regenerar e atualizar no `application.yml` do backend se quiser.

### 3.3 Criando as Client Roles

**Client Role** e uma role que existe apenas dentro deste client. Neste caso as roles representam operacoes que o usuario pode fazer com documentos.

Aba **Roles** → **Create role**:

| Nome da role | Descricao |
|---|---|
| `documentos:ler` | Permite ler documentos |
| `documentos:criar` | Permite criar documentos |
| `documentos:editar` | Permite editar documentos |
| `documentos:aprovar` | Permite aprovar documentos |
| `uma_protection` | Permite que o service account consulte a Protection API |

Crie uma por vez clicando em **Create role**, preenchendo o nome e salvando.

### 3.4 Configurando o Authorization Server

Aba **Authorization**. Aqui ficam os recursos, escopos, politicas e permissoes.

#### Scopes

**Authorization** → **Scopes** → **Create authorization scope**

Crie quatro scopes:
- `ler`
- `criar`
- `editar`
- `aprovar`

Esses scopes representam as acoes possiveis sobre um documento.

#### Resource

**Authorization** → **Resources** → **Create resource**

- **Name**: `documentos`
- **Display name**: `Documentos`
- **URIs**: `/documentos` e `/documentos/*` (adicione os dois)
- **Scopes**: selecione os quatro criados acima (`ler`, `criar`, `editar`, `aprovar`)

Salve.

#### Policies

**Policy** e a regra de decisao. Cada policy verifica se o usuario tem uma determinada role.

**Authorization** → **Policies** → **Create policy** → tipo **Role**

Crie quatro policies:

**Policy 1 — Somente leitores**
- Name: `Somente leitores`
- Realm Roles: (vazio)
- Client Roles: selecione `laboratorio-api` → `documentos:ler`
- Logic: Positive
- Salve

**Policy 2 — Somente criadores**
- Name: `Somente criadores`
- Client Roles: `laboratorio-api` → `documentos:criar`
- Salve

**Policy 3 — Somente editores**
- Name: `Somente editores`
- Client Roles: `laboratorio-api` → `documentos:editar`
- Salve

**Policy 4 — Somente aprovadores**
- Name: `Somente aprovadores`
- Client Roles: `laboratorio-api` → `documentos:aprovar`
- Salve

#### Permissions

**Permission** junta resource + scope + policy. Ela diz: "para fazer esta acao neste recurso, aplique esta regra".

**Authorization** → **Permissions** → **Create permission** → tipo **Scope**

Crie quatro permissions:

**Permission 1 — ler**
- Name: `Permissao para ler documentos`
- Resource: `documentos`
- Scope: `ler`
- Policy: `Somente leitores`
- Salve

**Permission 2 — criar**
- Name: `Permissao para criar documentos`
- Resource: `documentos`
- Scope: `criar`
- Policy: `Somente criadores`
- Salve

**Permission 3 — editar**
- Name: `Permissao para editar documentos`
- Resource: `documentos`
- Scope: `editar`
- Policy: `Somente editores`
- Salve

**Permission 4 — aprovar**
- Name: `Permissao para aprovar documentos`
- Resource: `documentos`
- Scope: `aprovar`
- Policy: `Somente aprovadores`
- Salve

#### Policy enforcement mode

Em **Authorization** → **Settings**:
- **Policy Enforcement Mode**: `Enforcing`
- **Allow Remote Resource Management**: ON

### 3.5 Protocol Mapper — incluir roles no token

Por padrao o token emitido pelo `laboratorio-frontend` nao inclui as roles do `laboratorio-api`. Precisamos de um mapper para isso.

No client `laboratorio-api` → aba **Client scopes** → clique no scope dedicado (geralmente `laboratorio-api-dedicated`) → **Add mapper** → **By configuration** → **User Client Role**

- **Name**: `client roles`
- **Client ID**: `laboratorio-api`
- **Token Claim Name**: `resource_access.${client_id}.roles`
- **Add to ID token**: ON
- **Add to access token**: ON
- **Add to userinfo**: ON
- **Multivalued**: ON

Salve.

---

## 4. Client `laboratorio-frontend`

Este client representa a SPA Vue. E um **public client** — nao tem secret porque roda no navegador e nao pode guardar segredos com seguranca.

### 4.1 Criando o client

**Clients** → **Create client**

- **Client ID**: `laboratorio-frontend`
- **Name**: Frontend do Laboratorio

Proxima tela:
- **Client authentication**: OFF (public client)
- **Authorization**: OFF
- **Authentication flow**: apenas Standard flow

Proxima tela:
- **Valid redirect URIs**: `http://localhost:5173/*`
- **Web origins**: `http://localhost:5173`

Salve.

### 4.2 Protocol Mapper — incluir roles do laboratorio-api no token

O frontend precisa que o token emitido por ele contenha as roles do `laboratorio-api` (para que o backend possa ler e o endpoint `/usuario/permissoes` funcione).

Client `laboratorio-frontend` → **Client scopes** → scope dedicado → **Add mapper** → **By configuration** → **User Client Role**

- **Name**: `client roles laboratorio-api`
- **Client ID**: `laboratorio-api`
- **Token Claim Name**: `resource_access.laboratorio-api.roles`
- **Add to ID token**: ON
- **Add to access token**: ON
- **Add to userinfo**: ON
- **Multivalued**: ON

Salve.

---

## 5. Client `laboratorio-menu`

Este client e o catalogo de menus. Ele nao e usado para login de usuarios — so o backend usa o service account dele para consultar a Admin API.

Aqui o Authorization Server esta habilitado mas sem policies nem permissions. Os **resources** sao usados como banco de dados de configuracao: cada resource e um item de menu com atributos customizados.

### 5.1 Criando o client

**Clients** → **Create client**

- **Client ID**: `laboratorio-menu`
- **Name**: Catalogo de Menu do Laboratorio

Proxima tela:
- **Client authentication**: ON
- **Authorization**: ON
- **Authentication flow**: desmarque tudo — este client nao faz login de usuario

Proxima tela:
- **Valid redirect URIs**: (deixe vazio)
- **Web origins**: (deixe vazio)

Salve.

Aba **Credentials** → anote o secret (o projeto usa `laboratorio-menu-secret`).

Em **Authorization** → **Settings**:
- **Policy Enforcement Mode**: `Permissive` (diferente do laboratorio-api que e Enforcing — aqui nao temos policies, entao tudo e permitido)

### 5.2 Client Roles do menu

Aba **Roles** → crie todas as roles abaixo:

**Menus principais:**

| Nome | Descricao |
|---|---|
| `menu:cadastros` | Permite ver o menu Cadastros |
| `menu:operacoes` | Permite ver o menu Operacoes |
| `menu:relatorios` | Permite ver o menu Relatorios |
| `menu:administracao` | Permite ver o menu Administracao |

**Submenus:**

| Nome | Descricao |
|---|---|
| `submenu:clientes` | Permite ver o submenu Clientes |
| `submenu:fornecedores` | Permite ver o submenu Fornecedores |
| `submenu:documentos` | Permite ver o submenu Documentos |
| `submenu:aprovacoes` | Permite ver o submenu Aprovacoes |
| `submenu:tarefas` | Permite ver o submenu Tarefas |
| `submenu:gerenciais` | Permite ver o submenu Gerenciais |
| `submenu:produtividade` | Permite ver o submenu Produtividade |
| `submenu:usuarios` | Permite ver o submenu Usuarios |
| `submenu:perfis` | Permite ver o submenu Perfis |

### 5.3 Resources do menu (o catalogo)

Cada item de menu e um resource com atributos customizados. O campo `roleNecessaria` indica qual Client Role o usuario precisa ter para ver aquele item.

**Authorization** → **Resources** → **Create resource**

Crie um resource para cada item abaixo. Em todos:
- **Type**: `frontend-menu`
- Os atributos ficam na secao **Attributes** (chave + valor)

---

**Cadastros** (menu pai)

- Name: `menu.cadastros`
- Display name: `Cadastros`
- URIs: `/tela/cadastros`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `cadastros` |
| `rotulo` | `Cadastros` |
| `rota` | `/tela/cadastros` |
| `tipo` | `menu` |
| `ordem` | `10` |
| `icone` | `Folder` |
| `roleNecessaria` | `menu:cadastros` |

---

**Clientes** (submenu de Cadastros)

- Name: `submenu.cadastros.clientes`
- Display name: `Clientes`
- URIs: `/tela/cadastros-clientes`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `cadastros-clientes` |
| `rotulo` | `Clientes` |
| `rota` | `/tela/cadastros-clientes` |
| `tipo` | `submenu` |
| `pai` | `cadastros` |
| `ordem` | `10` |
| `icone` | `Users` |
| `roleNecessaria` | `submenu:clientes` |

---

**Fornecedores** (submenu de Cadastros)

- Name: `submenu.cadastros.fornecedores`
- Display name: `Fornecedores`
- URIs: `/tela/cadastros-fornecedores`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `cadastros-fornecedores` |
| `rotulo` | `Fornecedores` |
| `rota` | `/tela/cadastros-fornecedores` |
| `tipo` | `submenu` |
| `pai` | `cadastros` |
| `ordem` | `20` |
| `icone` | `Building2` |
| `roleNecessaria` | `submenu:fornecedores` |

---

**Operacoes** (menu pai)

- Name: `menu.operacoes`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `operacoes` |
| `rotulo` | `Operacoes` |
| `rota` | `/tela/operacoes` |
| `tipo` | `menu` |
| `ordem` | `20` |
| `icone` | `Briefcase` |
| `roleNecessaria` | `menu:operacoes` |

---

**Documentos** (submenu de Operacoes)

- Name: `submenu.operacoes.documentos`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `operacoes-documentos` |
| `rotulo` | `Documentos` |
| `rota` | `/documentos` |
| `tipo` | `submenu` |
| `pai` | `operacoes` |
| `ordem` | `10` |
| `icone` | `FileText` |
| `roleNecessaria` | `submenu:documentos` |

---

**Aprovacoes** (submenu de Operacoes)

- Name: `submenu.operacoes.aprovacoes`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `operacoes-aprovacoes` |
| `rotulo` | `Aprovacoes` |
| `rota` | `/tela/operacoes-aprovacoes` |
| `tipo` | `submenu` |
| `pai` | `operacoes` |
| `ordem` | `20` |
| `icone` | `CheckCircle` |
| `roleNecessaria` | `submenu:aprovacoes` |

---

**Tarefas** (submenu de Operacoes)

- Name: `submenu.operacoes.tarefas`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `operacoes-tarefas` |
| `rotulo` | `Tarefas` |
| `rota` | `/tela/operacoes-tarefas` |
| `tipo` | `submenu` |
| `pai` | `operacoes` |
| `ordem` | `30` |
| `icone` | `ListChecks` |
| `roleNecessaria` | `submenu:tarefas` |

---

**Relatorios** (menu pai)

- Name: `menu.relatorios`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `relatorios` |
| `rotulo` | `Relatorios` |
| `rota` | `/tela/relatorios` |
| `tipo` | `menu` |
| `ordem` | `30` |
| `icone` | `BarChart3` |
| `roleNecessaria` | `menu:relatorios` |

---

**Gerenciais** (submenu de Relatorios)

- Name: `submenu.relatorios.gerenciais`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `relatorios-gerenciais` |
| `rotulo` | `Gerenciais` |
| `rota` | `/tela/relatorios-gerenciais` |
| `tipo` | `submenu` |
| `pai` | `relatorios` |
| `ordem` | `10` |
| `icone` | `BarChart3` |
| `roleNecessaria` | `submenu:gerenciais` |

---

**Produtividade** (submenu de Relatorios)

- Name: `submenu.relatorios.produtividade`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `relatorios-produtividade` |
| `rotulo` | `Produtividade` |
| `rota` | `/tela/relatorios-produtividade` |
| `tipo` | `submenu` |
| `pai` | `relatorios` |
| `ordem` | `20` |
| `icone` | `Gauge` |
| `roleNecessaria` | `submenu:produtividade` |

---

**Administracao** (menu pai)

- Name: `menu.administracao`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `administracao` |
| `rotulo` | `Administracao` |
| `rota` | `/tela/administracao` |
| `tipo` | `menu` |
| `ordem` | `40` |
| `icone` | `Settings` |
| `roleNecessaria` | `menu:administracao` |

---

**Usuarios** (submenu de Administracao)

- Name: `submenu.administracao.usuarios`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `administracao-usuarios` |
| `rotulo` | `Usuarios` |
| `rota` | `/tela/administracao-usuarios` |
| `tipo` | `submenu` |
| `pai` | `administracao` |
| `ordem` | `10` |
| `icone` | `UserCog` |
| `roleNecessaria` | `submenu:usuarios` |

---

**Perfis** (submenu de Administracao)

- Name: `submenu.administracao.perfis`
- Atributos:

| Chave | Valor |
|---|---|
| `id` | `administracao-perfis` |
| `rotulo` | `Perfis` |
| `rota` | `/tela/administracao-perfis` |
| `tipo` | `submenu` |
| `pai` | `administracao` |
| `ordem` | `20` |
| `icone` | `Shield` |
| `roleNecessaria` | `submenu:perfis` |

---

### 5.4 Permissoes do Service Account do laboratorio-menu

O backend usa o service account do `laboratorio-menu` para chamar a Admin API do Keycloak (listar resources e buscar roles de usuarios). Esse service account precisa de permissoes no realm.

**Users** → busque `service-account-laboratorio-menu` → aba **Role mapping** → **Assign role**

Filtre por `realm-management` e adicione:
- `manage-authorization`
- `manage-clients`
- `query-clients`
- `query-users`
- `view-authorization`
- `view-clients`
- `view-users`

---

## 6. Criando usuarios

Agora vamos criar os usuarios de teste e atribuir as roles corretas a cada um.

### 6.1 Como criar um usuario (exemplo completo: `leitor`)

**Users** → **Create new user**

- **Username**: `leitor`
- **Email**: `leitor@laboratorio.local`
- **Email verified**: ON
- **First name**: `Lia`
- **Last name**: `Leitora`

Salve.

Aba **Credentials** → **Set password**:
- **Password**: `senha123`
- **Temporary**: OFF (se deixar ON o usuario vai ser forcado a trocar a senha no primeiro login)

Salve.

Aba **Role mapping** → **Assign role**

Filtre por client `laboratorio-api` e adicione:
- `documentos:ler`

Filtre por client `laboratorio-menu` e adicione:
- `menu:operacoes`
- `submenu:documentos`
- `submenu:tarefas`

### 6.2 Todos os usuarios e suas roles

Repita o processo acima para cada usuario abaixo.

---

**editor** — Edu Editor — `editor@laboratorio.local`

Roles em `laboratorio-api`:
- `documentos:ler`
- `documentos:criar`
- `documentos:editar`

Roles em `laboratorio-menu`:
- `menu:cadastros`
- `menu:operacoes`
- `submenu:clientes`
- `submenu:fornecedores`
- `submenu:documentos`
- `submenu:tarefas`

---

**aprovador** — Ana Aprovadora — `aprovador@laboratorio.local`

Roles em `laboratorio-api`:
- `documentos:ler`
- `documentos:aprovar`

Roles em `laboratorio-menu`:
- `menu:operacoes`
- `menu:relatorios`
- `submenu:documentos`
- `submenu:aprovacoes`
- `submenu:gerenciais`

---

**gestor** — Gabi Gestora — `gestor@laboratorio.local`

Roles em `laboratorio-api`:
- `documentos:ler`
- `documentos:aprovar`

Roles em `laboratorio-menu`:
- `menu:cadastros`
- `menu:operacoes`
- `menu:relatorios`
- `submenu:clientes`
- `submenu:documentos`
- `submenu:aprovacoes`
- `submenu:gerenciais`
- `submenu:produtividade`

---

**administrador** — Artur Administrador — `administrador@laboratorio.local`

Roles em `laboratorio-api`:
- `documentos:ler`
- `documentos:criar`
- `documentos:editar`
- `documentos:aprovar`

Roles em `laboratorio-menu`:
- `menu:cadastros`
- `menu:operacoes`
- `menu:relatorios`
- `menu:administracao`
- `submenu:clientes`
- `submenu:fornecedores`
- `submenu:documentos`
- `submenu:aprovacoes`
- `submenu:tarefas`
- `submenu:gerenciais`
- `submenu:produtividade`
- `submenu:usuarios`
- `submenu:perfis`

---

## 7. O que cada usuario ve no menu

| Usuario | Menus visiveis |
|---|---|
| `leitor` | Operacoes → Documentos, Tarefas |
| `editor` | Cadastros → Clientes, Fornecedores / Operacoes → Documentos, Tarefas |
| `aprovador` | Operacoes → Documentos, Aprovacoes / Relatorios → Gerenciais |
| `gestor` | Cadastros → Clientes / Operacoes → Documentos, Aprovacoes / Relatorios → Gerenciais, Produtividade |
| `administrador` | Tudo |

---

## 8. Verificando se funcionou

Com o backend e frontend rodando, acesse `http://localhost:5173`, entre com qualquer usuario e:

1. O menu no topo deve aparecer de acordo com as roles do usuario
2. Em `/laboratorio`, o painel de permissoes mostra `documentos:ler`, `documentos:criar`, etc.
3. Em `/documentos`, os botoes de criar/editar/aprovar aparecem habilitados ou desabilitados conforme as permissoes
4. Trocar de usuario mostra menus e botoes diferentes

Se o menu nao aparecer, verifique:
- O service account do `laboratorio-menu` tem as roles do `realm-management`
- Os resources tem o atributo `roleNecessaria` preenchido corretamente
- O usuario tem a client role correspondente no `laboratorio-menu`
