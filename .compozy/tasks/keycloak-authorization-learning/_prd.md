# PRD: Laboratorio de Autorizacao com Keycloak

## Overview

O Laboratorio de Autorizacao com Keycloak e uma experiencia de aprendizado em portugues do Brasil para um desenvolvedor que esta comecando do zero e quer entender Keycloak Authorization Services na pratica. O produto deve permitir que o usuario rode um ambiente local, teste endpoints pelo Postman e entenda a diferenca entre acesso publico, acesso autenticado e acesso autorizado por recurso, escopo, politica e permissao.

A V1 sera um laboratorio guiado e enxuto, sem frontend. O dominio de exemplo sera `documentos`, com perfis `leitor`, `editor` e `aprovador`. O Keycloak deve iniciar com configuracao pronta para uso, e a experiencia de aprendizado deve conduzir o usuario primeiro por um roteiro guiado no Postman e depois por desafios pequenos.

O valor do produto esta em transformar conceitos abstratos de autorizacao em comportamentos observaveis: chamadas que retornam `200`, `401` e `403`, acompanhadas de explicacoes em PT-BR sobre por que cada resultado aconteceu.

## Goals

- Permitir que o usuario rode o laboratorio local completo em ate 20 minutos.
- Demonstrar pelo menos quatro categorias de acesso: publico, nao autenticado, autenticado sem permissao e autorizado.
- Ensinar a cadeia conceitual URI -> recurso -> escopo -> politica -> permissao -> resultado HTTP.
- Fazer o usuario testar os cenarios principais pelo Postman sem depender de frontend.
- Permitir que o usuario altere ou crie uma permissao simples e valide o novo comportamento.
- Reduzir carga cognitiva usando nomes, exemplos e documentacao em portugues do Brasil.

## User Stories

- Como desenvolvedor brasileiro aprendendo Keycloak, quero subir o laboratorio do zero para nao depender de configuracao externa.
- Como aprendiz, quero chamar um endpoint publico sem token para entender que algumas rotas podem ficar livres.
- Como aprendiz, quero chamar um endpoint protegido sem token e receber `401` para entender a diferenca entre estar anonimo e estar logado.
- Como aprendiz, quero chamar um endpoint autenticado com token valido para entender o papel da autenticacao.
- Como aprendiz, quero chamar um endpoint autorizado com um usuario sem permissao e receber `403` para entender que autenticacao nao basta.
- Como aprendiz, quero comparar usuarios `leitor`, `editor` e `aprovador` para visualizar permissoes progressivas.
- Como aprendiz, quero seguir uma colecao Postman guiada para ver os cenarios em ordem.
- Como aprendiz, quero fazer desafios curtos para provar que consigo explicar e modificar permissao.

## Core Features

- **Ambiente local pronto para estudo:** o usuario deve conseguir iniciar o Keycloak e a aplicacao demo localmente com configuracao inicial pronta para teste.
- **Dominio `documentos`:** o laboratorio deve usar um dominio simples o suficiente para demonstrar leitura, criacao, edicao e aprovacao sem adicionar complexidade de negocio desnecessaria.
- **Perfis de aprendizado:** a V1 deve incluir os perfis `leitor`, `editor` e `aprovador`, cada um com permissoes progressivas e cenarios claros de acesso permitido ou negado.
- **Endpoints publicos:** deve existir pelo menos um endpoint livre para demonstrar acesso sem token.
- **Endpoints autenticados:** deve existir pelo menos um endpoint que exige login, mas nao exige autorizacao fina por recurso/politica/permissao.
- **Endpoints autorizados:** deve existir um conjunto de endpoints de `documentos` que dependem de autorizacao fina no Keycloak.
- **Cenarios negativos de primeira classe:** a experiencia deve incluir casos esperados de `401` e `403`, explicando o motivo do bloqueio.
- **Colecao Postman hibrida:** a colecao deve ter uma parte guiada, com ordem recomendada, e uma parte de desafios pequenos para o usuario prever, alterar e validar permissoes.
- **Documentacao didatica em PT-BR:** a documentacao deve explicar os conceitos e mapear cada endpoint aos seus recursos, escopos, politicas e permissoes.

## User Experience

A jornada principal comeca com o usuario abrindo o README e seguindo instrucoes de preparacao local. Em seguida, ele inicia o ambiente e acessa o Keycloak apenas quando precisar observar ou alterar permissoes. O primeiro contato pratico deve acontecer no Postman.

O roteiro guiado do Postman deve conduzir o usuario por esta sequencia:

1. Chamar endpoint publico e observar `200`.
2. Chamar endpoint autenticado sem token e observar `401`.
3. Obter token para um usuario de exemplo.
4. Chamar endpoint autenticado e observar `200`.
5. Chamar endpoint autorizado com usuario sem permissao e observar `403`.
6. Chamar endpoint autorizado com usuario correto e observar `200`.
7. Comparar os perfis `leitor`, `editor` e `aprovador`.
8. Fazer um desafio final alterando ou criando permissao e validando o resultado.

A experiencia deve evitar linguagem excessivamente academica. Cada conceito deve ser explicado com uma frase curta, um exemplo concreto e um resultado observavel.

## High-Level Technical Constraints

- A V1 deve ser executavel localmente e partir de um ambiente sem configuracao previa de Keycloak.
- A experiencia deve usar Keycloak em ambiente local de desenvolvimento.
- A configuracao inicial de realm, usuarios, perfis e permissoes deve estar disponivel automaticamente para reduzir atrito.
- O produto deve permanecer backend-first e nao incluir frontend na V1.
- A documentacao e os exemplos devem priorizar portugues do Brasil.
- O laboratorio deve manter uma separacao clara entre autenticacao e autorizacao.
- O modo padrao de autorizacao deve ensinar negacao por padrao; modos permissivos devem ser tratados apenas como contraste didatico.

## Non-Goals (Out of Scope)

- Criar frontend na V1.
- Cobrir todos os tipos de policy do Keycloak.
- Ensinar hardening de producao, TLS, segredos reais ou operacao segura em ambiente corporativo.
- Criar multiplos microservicos.
- Construir um sistema real de gestao documental.
- Criar um curso completo de Keycloak.
- Ensinar configuracao manual completa do Admin Console como fluxo principal da V1.

## Phased Rollout Plan

### MVP (Phase 1)

- Laboratorio local com Keycloak configurado automaticamente.
- Dominio `documentos`.
- Perfis `leitor`, `editor` e `aprovador`.
- Endpoints publicos, autenticados, autorizados e negados.
- Colecao Postman com roteiro guiado.
- Documentacao PT-BR explicando cada cenario.
- Pelo menos um desafio pratico de permissao.

Success criteria to proceed:

- Usuario consegue rodar tudo localmente.
- Usuario consegue obter token e executar a colecao principal.
- Usuario observa `200`, `401` e `403` em cenarios intencionais.
- Usuario consegue explicar por que um endpoint foi permitido ou negado.

### Phase 2

- Adicionar mais desafios praticos.
- Incluir uma secao de recriacao manual da configuracao no Keycloak Admin Console.
- Expandir explicacoes sobre modos `ENFORCING`, `PERMISSIVE` e `DISABLED`.
- Melhorar exemplos de troubleshooting.

Success criteria to proceed:

- Usuario consegue recriar parte da configuracao sem depender apenas do import.
- Usuario consegue alterar uma permissao e prever o efeito antes de testar.

### Phase 3

- Adicionar frontend consumindo a mesma base de aprendizado.
- Incluir visualizacao de permissoes por perfil.
- Expandir para cenarios mais realistas ou dominios adicionais.
- Transformar o laboratorio em trilha de estudo reutilizavel.

Success criteria:

- Frontend reforca o aprendizado sem esconder a relacao entre token, endpoint e permissao.
- Novos cenarios continuam explicaveis em PT-BR.

## Success Metrics

- **Setup local:** usuario roda o laboratorio completo em ate 20 minutos.
- **Cobertura de endpoints:** no minimo 8 endpoints demonstrativos.
- **Cobertura de cenarios:** pelo menos 4 categorias: publico, `401`, `403` e autorizado `200`.
- **Postman:** no minimo 10 requisicoes organizadas entre roteiro guiado e desafios.
- **Conceitos explicados:** no minimo 8 conceitos em PT-BR: recurso, escopo, politica, permissao, URI, token, autenticado e autorizado/negado.
- **Exercicio final:** pelo menos 1 desafio em que o usuario altera ou cria permissao e valida o resultado.
- **Compreensao:** usuario consegue explicar a diferenca entre endpoint publico, autenticado e autorizado usando exemplos do laboratorio.

## Risks and Mitigations

- **Risco: o usuario apenas roda a colecao sem entender.**
  Mitigacao: incluir explicacoes curtas entre os passos e desafios que exigem previsao do resultado.

- **Risco: o import automatico parecer magica.**
  Mitigacao: documentar o mapa entre endpoint, recurso, escopo, politica e permissao.

- **Risco: confundir autenticacao com autorizacao.**
  Mitigacao: separar endpoints autenticados de endpoints autorizados e incluir `403` como cenario principal.

- **Risco: escopo crescer com frontend ou muitos cenarios.**
  Mitigacao: manter frontend e catalogo avancado fora da V1.

- **Risco: termos do Keycloak continuarem abstratos.**
  Mitigacao: sempre ligar conceito a endpoint, status HTTP e usuario de exemplo.

## Architecture Decision Records

- [ADR-001: Escopo da V1 para Laboratorio PT-BR de Autorizacao com Keycloak](adrs/adr-001.md) — Define uma V1 backend-first, dockerizada, testada via Postman e com frontend adiado para V2+.
- [ADR-002: Abordagem de Produto da V1 como Laboratorio Guiado e Enxuto](adrs/adr-002.md) — Define a abordagem guiada e enxuta com dominio `documentos`, import automatico e desafios pequenos.

## Open Questions

- Quais nomes concretos os usuarios de exemplo devem ter?
- Quais endpoints exatos de `documentos` entram no roteiro guiado?
- O desafio final deve pedir criacao de uma nova permissao ou alteracao de uma permissao existente?
- A documentacao deve incluir uma secao curta de glossario antes ou depois do roteiro Postman?
