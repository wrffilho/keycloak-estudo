# Spring Resource Server vs Policy Enforcer

Este laboratorio usa uma abordagem hibrida:

- O Spring Security Resource Server valida o JWT emitido pelo Keycloak.
- A API converte roles/scopes do token em permissoes como `documentos:ler`.
- O Keycloak ainda modela recursos, escopos, politicas e permissoes para fins de aprendizado.

## Por que nao usar Policy Enforcer direto na V1?

O Policy Enforcer deixa o Keycloak mais responsavel por decidir acesso por path, mas adiciona uma camada especifica de integracao. Para aprender do zero, primeiro queremos enxergar:

1. token recebido;
2. permissao dentro do token;
3. endpoint exigindo permissao;
4. resposta `200`, `401` ou `403`.

Depois que isso estiver claro, uma proxima etapa pode implementar o mesmo dominio usando Policy Enforcer para comparar as abordagens.
