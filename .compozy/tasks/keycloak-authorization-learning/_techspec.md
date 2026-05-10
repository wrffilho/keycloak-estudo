# TechSpec: Laboratorio de Autorizacao com Keycloak

## Executive Summary

A V1 sera implementada como uma API Java + Spring Boot, backend-first, organizada como monolito modular simples. O laboratorio usara Keycloak local via Docker Compose, realm importado automaticamente, dados de `documentos` em memoria, colecao Postman e testes essenciais para demonstrar acesso publico, autenticado, autorizado e negado.

A principal troca tecnica e escolher uma abordagem hibrida de autorizacao: usar Spring Security Resource Server para validar JWT e aplicar permissoes/scopes nos endpoints, enquanto o Keycloak mantem recursos, escopos, politicas e permissoes configurados e documentados. Isso reduz acoplamento inicial ao Policy Enforcer, mas exige documentacao clara para explicar onde a decisao acontece.

## System Architecture

### Component Overview

- **Backend Spring Boot:** aplicacao principal do laboratorio, localizada em `backend/`. Expoe endpoints publicos, autenticados e autorizados para o dominio `documentos`.
- **Modulo `documentos`:** concentra modelo, servico em memoria e endpoints do dominio. Deve ser pequeno, previsivel e didatico.
- **Modulo `seguranca`:** concentra regras de acesso, conversao de claims/scopes do JWT em authorities e helpers para checagem de permissao.
- **Modulo `configuracao`:** concentra configuracoes de seguranca, CORS se necessario para fase futura, resource server e propriedades do Keycloak.
- **Keycloak local:** provedor de identidade e autorizacao. Deve subir via Docker Compose com realm importado automaticamente.
- **Realm importado:** contem client, usuarios `leitor`, `editor`, `aprovador`, recursos, escopos, politicas e permissoes.
- **Postman Collection:** roteiro guiado e desafios pequenos para validar `200`, `401` e `403`.
- **Testes essenciais:** validam comportamento minimo dos endpoints e regras de acesso.

Fluxo principal:

1. Usuario sobe Keycloak e backend localmente.
2. Usuario obtem token no Postman usando um usuario de exemplo.
3. Backend valida JWT via Spring Security Resource Server.
4. Claims/scopes do token viram authorities.
5. Endpoints publicos, autenticados ou autorizados respondem com `200`, `401` ou `403`.
6. Documentacao mostra como o resultado se conecta a recurso, escopo, politica e permissao no Keycloak.

Estrutura fisica planejada para evoluir com frontend futuro:

- `backend/`: aplicacao Java + Spring Boot em monolito modular simples.
- `infra/`: arquivos de infraestrutura local, incluindo import do realm Keycloak.
- `postman/`: colecao e ambiente para validacao manual.
- `docs/`: material de apoio em PT-BR.

## Implementation Design

### Core Interfaces

```java
public interface ServicoDeDocumentos {
    List<DocumentoResposta> listar();
    DocumentoResposta buscarPorId(String id);
    DocumentoResposta criar(CriarDocumentoRequisicao requisicao);
    DocumentoResposta editar(String id, EditarDocumentoRequisicao requisicao);
    DocumentoResposta aprovar(String id);
}
```

```java
public record DocumentoResposta(
    String id,
    String titulo,
    String conteudo,
    String status
) {}
```

```java
public interface LeitorDePermissoes {
    boolean possuiPermissao(String nomeDaPermissao);
    Set<String> listarPermissoes();
}
```

### Data Models

- **Documento**
  - `id: String`
  - `titulo: String`
  - `conteudo: String`
  - `status: String` (`RASCUNHO`, `PUBLICADO`, `APROVADO`)
  - `criadoPor: String`

- **CriarDocumentoRequisicao**
  - `titulo: String`
  - `conteudo: String`

- **EditarDocumentoRequisicao**
  - `titulo: String`
  - `conteudo: String`

- **DocumentoResposta**
  - `id: String`
  - `titulo: String`
  - `conteudo: String`
  - `status: String`

- **Permissoes esperadas**
  - `documentos:ler`
  - `documentos:criar`
  - `documentos:editar`
  - `documentos:aprovar`

Dados ficam em memoria na V1. A lista inicial deve conter documentos suficientes para chamadas de leitura, edicao e aprovacao.

### API Endpoints

#### Publicos

- `GET /publico/status`
  - Descricao: confirma que a aplicacao esta rodando.
  - Auth: nenhuma.
  - Respostas: `200`.

- `GET /publico/sobre`
  - Descricao: explica brevemente o laboratorio.
  - Auth: nenhuma.
  - Respostas: `200`.

#### Autenticados

- `GET /usuario/perfil`
  - Descricao: retorna informacoes basicas extraidas do token.
  - Auth: token valido.
  - Respostas: `200`, `401`.

- `GET /usuario/permissoes`
  - Descricao: mostra permissoes/scopes percebidos pela aplicacao.
  - Auth: token valido.
  - Respostas: `200`, `401`.

#### Autorizados por documentos

- `GET /documentos`
  - Permissao: `documentos:ler`.
  - Respostas: `200`, `401`, `403`.

- `GET /documentos/{id}`
  - Permissao: `documentos:ler`.
  - Respostas: `200`, `401`, `403`, `404`.

- `POST /documentos`
  - Permissao: `documentos:criar`.
  - Respostas: `201`, `401`, `403`, `400`.

- `PUT /documentos/{id}`
  - Permissao: `documentos:editar`.
  - Respostas: `200`, `401`, `403`, `404`, `400`.

- `POST /documentos/{id}/aprovar`
  - Permissao: `documentos:aprovar`.
  - Respostas: `200`, `401`, `403`, `404`.

### Keycloak Authorization Mapping

A configuracao importada deve mapear:

| Endpoint | Recurso | Escopo | Permissao |
|---|---|---|---|
| `GET /documentos` | `documentos` | `ler` | `documentos:ler` |
| `GET /documentos/{id}` | `documentos` | `ler` | `documentos:ler` |
| `POST /documentos` | `documentos` | `criar` | `documentos:criar` |
| `PUT /documentos/{id}` | `documentos` | `editar` | `documentos:editar` |
| `POST /documentos/{id}/aprovar` | `documentos` | `aprovar` | `documentos:aprovar` |

Usuarios/perfis:

| Usuario/Perfil | Permissoes |
|---|---|
| `leitor` | `documentos:ler` |
| `editor` | `documentos:ler`, `documentos:criar`, `documentos:editar` |
| `aprovador` | `documentos:ler`, `documentos:aprovar` |

## Integration Points

### Keycloak

- A API deve confiar no issuer local do Keycloak para validar tokens JWT.
- O realm importado deve conter usuarios, client, recursos, escopos, politicas e permissoes.
- O backend deve converter claims/scopes do token em authorities usadas pelas regras de endpoint.
- Falhas de autenticacao devem retornar `401`.
- Falhas de autorizacao devem retornar `403`.

### Postman

- A colecao deve conter requests para obter token dos usuarios de exemplo.
- A colecao deve conter requests guiadas para todos os endpoints.
- A colecao deve separar cenario feliz, cenario sem token e cenario sem permissao.
- Variaveis de ambiente devem reduzir copia manual de tokens.

## Impact Analysis

| Component | Impact Type | Description and Risk | Required Action |
|---|---|---|---|
| Backend Spring Boot | new | Aplicacao principal do laboratorio | Criar projeto, endpoints e configuracao de seguranca |
| Documentos | new | Dominio de exemplo para autorizacao | Criar modelo, servico em memoria e endpoints |
| Seguranca | new | Validacao JWT e conversao de permissoes | Configurar resource server e authorities |
| Keycloak Docker | new | Ambiente local de IAM | Criar compose e realm importavel |
| Postman | new | Experiencia de validacao manual | Criar colecao e ambiente |
| Documentacao | new | Guia de aprendizado PT-BR | Criar README, mapa de permissoes e troubleshooting |
| Testes | new | Rede minima de regressao | Criar testes para `200`, `401` e `403` |

## Testing Approach

### Unit Tests

- Testar `ServicoDeDocumentos` com dados em memoria.
- Testar `LeitorDePermissoes` ou conversor de authorities com claims simuladas.
- Testar casos de documento inexistente e dados invalidos quando aplicavel.

### Integration Tests

- Testar endpoints publicos sem token retornando `200`.
- Testar endpoints autenticados sem token retornando `401`.
- Testar endpoints autenticados com token simulado retornando `200`.
- Testar endpoints autorizados sem permissao retornando `403`.
- Testar endpoints autorizados com permissao retornando `200`.

A V1 deve evitar uma suite pesada dependente de Keycloak real em todos os testes. A validacao integrada com Keycloak real pode ser coberta por Postman e documentacao operacional.

## Development Sequencing

### Build Order

1. Criar estrutura base do projeto Spring Boot - sem dependencias internas.
2. Criar modulo `documentos` com dados em memoria - depende do passo 1.
3. Criar endpoints publicos e autenticados basicos - depende do passo 1.
4. Configurar Spring Security Resource Server - depende do passo 1.
5. Criar modulo `seguranca` para converter claims/scopes em authorities - depende do passo 4.
6. Proteger endpoints de `documentos` por permissao - depende dos passos 2 e 5.
7. Criar Docker Compose do Keycloak - depende do passo 1 apenas como referencia de portas/configuracao.
8. Criar realm importado com usuarios, client, recursos, escopos, politicas e permissoes - depende dos passos 6 e 7 para alinhar endpoints e permissoes.
9. Criar colecao Postman e ambiente - depende dos passos 6, 7 e 8.
10. Criar testes essenciais - depende dos passos 3, 4, 5 e 6.
11. Criar README e documentacao PT-BR - depende dos passos 7, 8, 9 e 10.
12. Revisar fluxo completo do zero - depende de todos os passos anteriores.

### Technical Dependencies

- Java instalado localmente.
- Docker disponivel para subir Keycloak.
- Postman instalado ou alternativa compativel para importar colecao.
- Acesso a imagens Docker do Keycloak.
- Definicao final das portas locais usadas por backend e Keycloak.

## Monitoring and Observability

Por ser laboratorio local, observabilidade deve ser simples:

- Logs de inicializacao indicando issuer configurado.
- Logs de erro para `401` e `403` em nivel apropriado, sem vazar token.
- Endpoint publico de status para confirmar que a API esta rodando.
- Mensagens de erro didaticas em respostas controladas quando seguro.

Nao ha alertas operacionais na V1.

## Technical Considerations

### Key Decisions

- **Java + Spring Boot:** escolhido por alinhamento com Keycloak, Spring Security e aprendizado backend.
- **Monolito modular simples:** escolhido para aprender organizacao modular sem criar projeto grande demais.
- **Dados em memoria:** escolhido para manter foco em autorizacao.
- **Spring Resource Server + mapeamento Keycloak:** escolhido para equilibrar stack moderna e aprendizado de Authorization Services.
- **Postman como experiencia principal:** escolhido para expor token, status HTTP e permissoes sem abstrair por frontend.
- **Testes essenciais:** escolhidos para proteger cenarios centrais sem virar suite pesada.

### Known Risks

- **Claims do token nao refletirem permissoes como esperado:** prototipar cedo o formato do token e documentar o mapeamento.
- **Import do realm ficar dificil de manter:** manter realm pequeno e com nomes didaticos.
- **Confusao entre roles, scopes e permissoes:** usar glossario e endpoint `/usuario/permissoes`.
- **Policy Enforcer parecer ignorado:** incluir secao de comparacao e explicar por que ele fica fora do mecanismo principal da V1.
- **Monolito modular virar excesso de estrutura:** criar apenas pacotes necessarios para a V1.

## Architecture Decision Records

- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — Define V1 backend-first, Docker, Postman e frontend adiado.
- [ADR-002: Abordagem de Produto da V1 como Laboratorio Guiado e Enxuto](adrs/adr-002.md) — Define laboratorio guiado com dominio `documentos`, import automatico e desafios.
- [ADR-003: Stack Java Spring Boot com Monolito Modular Simples](adrs/adr-003.md) — Define Java + Spring Boot e organizacao modular simples.
- [ADR-004: Autorizacao Hibrida com Spring Resource Server e Mapeamento Keycloak](adrs/adr-004.md) — Define JWT/scopes no Spring com mapeamento didatico de Authorization Services no Keycloak.
- [ADR-005: Dados em Memoria e Testes Essenciais para V1](adrs/adr-005.md) — Define dados em memoria e cobertura minima dos cenarios centrais.
