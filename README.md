# Laboratorio de Autorizacao com Keycloak

Laboratorio em portugues do Brasil para aprender a diferenca entre endpoint publico, endpoint autenticado e endpoint autorizado com Keycloak.

Projeto versionado para estudo com Git e GitHub via SSH.

## Requisitos

- Java 21
- Maven 3.9+
- Docker
- Postman

## Estrutura do projeto

```text
backend/      API Spring Boot em monolito modular
infra/        Import do realm Keycloak
postman/      Colecao e ambiente para teste manual
docs/         Explicacoes em PT-BR
```

## Subir Keycloak

```bash
docker compose up -d keycloak
```

Admin Console:

- URL: http://localhost:8080
- Usuario: `admin`
- Senha: `admin`

Realm importado:

- `laboratorio-keycloak`

Client usado pelos tokens:

- Client ID: `laboratorio-api`
- Client Secret: `laboratorio-api-secret`

## Rodar a API

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

### Autorizados

| Metodo | URI | Permissao |
|---|---|---|
| GET | `/documentos` | `documentos:ler` |
| GET | `/documentos/{id}` | `documentos:ler` |
| POST | `/documentos` | `documentos:criar` |
| PUT | `/documentos/{id}` | `documentos:editar` |
| POST | `/documentos/{id}/aprovar` | `documentos:aprovar` |

## Mapa Keycloak

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
- [Fluxo completo do endpoint POST /documentos](docs/fluxo-endpoint-criar-documento.md)
- [hasRole, hasAuthority e hasPermission](docs/hasrole-hasauthority-haspermission.md)
- [SecurityContextHolder, SecurityContext e Authentication](docs/security-context-holder-security-context-authentication.md)
