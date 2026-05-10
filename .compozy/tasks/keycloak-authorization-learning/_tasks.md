# Laboratorio de Autorizacao com Keycloak — Task List

## Tasks

| # | Title | Status | Complexity | Dependencies |
|---|-------|--------|------------|--------------|
| 01 | Criar base Spring Boot do backend | completed | medium | — |
| 02 | Implementar dominio `documentos` em memoria | completed | medium | task_01 |
| 03 | Criar endpoints publicos e autenticados | completed | medium | task_01 |
| 04 | Configurar seguranca JWT com Spring Resource Server | completed | high | task_01 |
| 05 | Implementar conversao de permissoes e endpoints autorizados | completed | high | task_02, task_04 |
| 06 | Criar ambiente Docker do Keycloak | completed | medium | task_01 |
| 07 | Criar realm importado com usuarios, client e permissoes | completed | high | task_05, task_06 |
| 08 | Criar colecao Postman guiada e desafios | completed | medium | task_05, task_07 |
| 09 | Criar documentacao PT-BR do laboratorio | completed | medium | task_06, task_07, task_08 |
| 10 | Consolidar testes essenciais e validacao final do fluxo | completed | high | task_03, task_05, task_07, task_09 |
