# Laboratorio de Autorizacao com Keycloak

Laboratorio em portugues do Brasil para aprender a diferenca entre endpoint publico, endpoint autenticado e endpoint autorizado com Keycloak.

Projeto versionado para estudo com Git e GitHub via SSH.

## Requisitos

- Java 21
- Maven 3.9+
- Node.js 22+
- Docker
- Postman

## Estrutura do projeto

```text
backend/      API Spring Boot em monolito modular
frontend/     Painel web em Vue para testar o laboratorio
infra/        Import do realm Keycloak
postman/      Colecao e ambiente para teste manual
docs/         Explicacoes em PT-BR
```

## Subir tudo com Docker

Se for a primeira vez, ou se voce ja sabe que o Keycloak esta atualizado, este comando sobe o Keycloak, o backend e o frontend:

```bash
docker compose up -d --build
```

Se aparecer no login do Keycloak a mensagem `Client not found`, significa que o container do Keycloak ainda esta usando um realm antigo, sem o client `laboratorio-frontend`. Nesse caso, pare e recrie os containers para o Keycloak importar o realm atualizado:

```bash
docker compose down
docker compose up -d --build
```

Servicos:

- Keycloak: http://localhost:8080
- Backend: http://localhost:8081
- Frontend: http://localhost:5173

Para parar os containers:

```bash
docker compose down
```

## Subir somente a API com Docker

Use este comando quando o Keycloak ja estiver rodando e voce quiser subir ou reconstruir somente o backend:

```bash
docker compose up -d --build backend
```

## Subir somente o frontend com Docker

Use este comando quando Keycloak e backend ja estiverem rodando:

```bash
docker compose up -d --build frontend
```

## Rodar somente o frontend em desenvolvimento

Use esta opcao quando Keycloak e backend ja estiverem rodando:

```bash
cd frontend
npm install
npm run dev
```

## Subir somente o Keycloak

Use este comando quando quiser rodar apenas o Keycloak no Docker e iniciar o backend pela sua maquina com Maven:

```bash
docker compose up -d keycloak
```

Admin Console:

- URL: http://localhost:8080
- Usuario: `admin`
- Senha: `admin`

Realm importado:

- `laboratorio-keycloak`

Tema de login:

- `laboratorio`
- Arquivos em `infra/keycloak/themes/laboratorio/login`

Client usado pelos tokens:

- Client ID: `laboratorio-api`
- Client Secret: `laboratorio-api-secret`

Client usado pelo frontend:

- Client ID: `laboratorio-frontend`
- Tipo: publico, sem secret

Client usado pelo catalogo de menu dinamico:

- Client ID: `laboratorio-menu`
- Tipo: confidencial
- Client Secret: `laboratorio-menu-secret`

## Rodar somente a API com Maven

Use esta opcao quando voce subiu somente o Keycloak com Docker:

```bash
mvn -pl backend spring-boot:run
```

A API sobe em:

```text
http://localhost:8081
```

## Usuarios do laboratorio

Todos usam a senha `senha123`.

| Usuario | Permissoes |
|---|---|
| `leitor` | `documentos:ler` |
| `editor` | `documentos:ler`, `documentos:criar`, `documentos:editar` |
| `aprovador` | `documentos:ler`, `documentos:aprovar` |
| `gestor` | usuario ficticio para menus corporativos |
| `administrador` | usuario ficticio para menus corporativos e administracao |

## Endpoints

### Publicos

| Metodo | URI | Resultado esperado |
|---|---|---|
| GET | `/publico/status` | `200` sem token |
| GET | `/publico/sobre` | `200` sem token |

### Autenticados

| Metodo | URI | Resultado esperado |
|---|---|---|
| GET | `/usuario/perfil` | `401` sem token, `200` com token |
| GET | `/usuario/permissoes` | `401` sem token, `200` com token |
| GET | `/usuario/menu` | `401` sem token, `200` com token |

### Autorizados

| Metodo | URI | Permissao |
|---|---|---|
| GET | `/documentos` | `documentos:ler` |
| GET | `/documentos/{id}` | `documentos:ler` |
| POST | `/documentos` | `documentos:criar` |
| PUT | `/documentos/{id}` | `documentos:editar` |
| POST | `/documentos/{id}/aprovar` | `documentos:aprovar` |

## Mapa Keycloak

Na branch `feature/policy-enforcer-keycloak`, este mapa tambem e usado pelo filtro `ServletPolicyEnforcerFilter`: o Spring autentica o JWT e o Keycloak decide se o usuario possui o escopo exigido para o path e metodo.

| URI | Recurso | Escopo | Permissao |
|---|---|---|---|
| `/documentos` | `documentos` | `ler` | `documentos:ler` |
| `/documentos/{id}` | `documentos` | `ler` | `documentos:ler` |
| `/documentos` | `documentos` | `criar` | `documentos:criar` |
| `/documentos/{id}` | `documentos` | `editar` | `documentos:editar` |
| `/documentos/{id}/aprovar` | `documentos` | `aprovar` | `documentos:aprovar` |

## Usar Postman

Importe:

- `postman/laboratorio-keycloak.postman_environment.json`
- `postman/laboratorio-keycloak.postman_collection.json`

Tambem existe uma variante mais parecida com collections exportadas pelo Postman, com `auth` Bearer na raiz e variavel `{{token}}`:

- `postman/laboratorio-keycloak-estilo-keycloak-projeto.postman_collection.json`

Execute os requests em ordem. O roteiro mostra:

1. endpoint publico retornando `200`;
2. endpoint autenticado sem token retornando `401`;
3. login dos usuarios;
4. endpoints permitidos retornando `200` ou `201`;
5. endpoints sem permissao retornando `403`;
6. desafio final para prever o resultado.

Os requests protegidos usam um pre-request script da collection. Ele le o header interno `X-Usuario-Teste`, busca o token no Keycloak automaticamente e envia `Authorization: Bearer ...` na chamada real. Os requests "Obter token" continuam na collection para estudo, mas nao sao obrigatorios para os demais requests funcionarem.

## Rodar testes

```bash
mvn test
```

Voce tambem pode entrar na pasta do backend e rodar os comandos por la:

```bash
cd backend
mvn spring-boot:run
mvn test
```

## 401 ou 403?

- `401`: a API nao reconheceu um usuario autenticado.
- `403`: a API reconheceu o usuario, mas ele nao possui a permissao exigida.

## Mais leitura

- [Glossario](docs/glossario.md)
- [Spring Resource Server vs Policy Enforcer](docs/spring-resource-server-vs-policy-enforcer.md)
- [Fluxo completo: login, menu, RPT e Policy Enforcer](docs/fluxo-completo-policy-enforcer-menu-rpt.md)
- [Fluxo completo do endpoint POST /documentos](docs/fluxo-endpoint-criar-documento.md)
- [Menu dinamico com Keycloak](docs/menu-dinamico-keycloak.md)
- [hasRole, hasAuthority e hasPermission](docs/hasrole-hasauthority-haspermission.md)
- [SecurityContextHolder, SecurityContext e Authentication](docs/security-context-holder-security-context-authentication.md)
