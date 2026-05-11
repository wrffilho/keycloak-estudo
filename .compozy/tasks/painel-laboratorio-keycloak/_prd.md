# PRD: Painel do Laboratorio Keycloak

## Overview

O Painel do Laboratorio Keycloak e um frontend em portugues do Brasil para transformar o laboratorio atual em uma experiencia visual, guiada e demonstravel. Ele e voltado para desenvolvedores brasileiros que querem entender, na pratica, como login, permissoes, endpoints e respostas HTTP se conectam em uma aplicacao protegida por Keycloak.

A V1 deve criar uma pasta `frontend/` na raiz do monolito modular e entregar uma experiencia com modo duplo: uma jornada guiada para aprendizado e uma area de uso livre para operar documentos. O produto deve manter a maxima do projeto: reduzir carga cognitiva usando PT-BR em telas, documentacao, exemplos e tambem no codigo do frontend, incluindo nomes de arquivos, funcoes, componentes e variaveis quando fizer sentido.

O valor do produto esta em tornar observavel a cadeia: usuario -> permissoes -> endpoint -> acao -> resposta. O painel nao deve virar um gerenciador documental completo, nem um clone de Postman. Ele deve ensinar autorizacao por meio de uma aplicacao pequena, realista e explicavel.

## Goals

- Permitir que o usuario execute uma jornada guiada de autorizacao em ate 5 minutos.
- Demonstrar login real via Keycloak dentro de uma experiencia visual.
- Mostrar permissoes efetivas do usuario sem fazer do token/claims o foco principal.
- Permitir listar, ver detalhe, criar, editar e aprovar documentos conforme permissoes.
- Mostrar claramente quando uma acao esta indisponivel e qual permissao falta.
- Exibir, para cada acao relevante, o endpoint chamado, a acao executada e o status retornado.
- Permitir subir a experiencia completa com Docker e tambem rodar o frontend separadamente para desenvolvimento.
- Manter consistencia PT-BR em pelo menos 90% dos textos, nomes visiveis e nomes de codigo do frontend.

## User Stories

- Como desenvolvedor brasileiro aprendendo Keycloak, quero entrar com login real para entender como uma aplicacao protegida se comporta.
- Como aprendiz, quero seguir uma jornada guiada para conectar usuario, permissoes, endpoint, acao e resposta.
- Como aprendiz, quero ver minhas permissoes efetivas para entender por que posso ou nao executar uma acao.
- Como aprendiz, quero ver acoes bloqueadas com explicacao da permissao ausente para entender autorizacao sem adivinhacao.
- Como aprendiz, quero operar documentos livremente depois da jornada guiada para testar o backend sem abrir Postman.
- Como apresentador do laboratorio, quero demonstrar o fluxo em uma tela organizada para explicar Keycloak para outra pessoa.
- Como mantenedor do projeto, quero que o frontend respeite o padrao PT-BR-first para manter baixa carga cognitiva.

## Core Features

- **Jornada guiada de autorizacao:** conduz o usuario por login, identificacao do usuario atual, permissoes disponiveis, acao em documento, endpoint relacionado e resposta observada.
- **Uso livre de documentos:** permite listar documentos, abrir detalhe, criar, editar e aprovar dentro das permissoes do usuario logado.
- **Painel de permissoes:** mostra permissoes efetivas de forma simples, sem exigir leitura de token bruto.
- **Acoes bloqueadas explicadas:** acoes sem permissao aparecem desabilitadas e informam qual permissao esta faltando.
- **Diagnostico de acao:** mostra a ultima acao relevante, endpoint, metodo conceitual, status retornado e explicacao curta.
- **Login e logout reais:** permite entrar e sair pelo Keycloak para testar usuarios como `leitor`, `editor` e `aprovador`.
- **Experiencia PT-BR-first:** usa portugues do Brasil na interface, mensagens, documentacao e nomes do codigo do frontend quando fizer sentido.
- **Comandos de execucao claros:** documenta caminho completo por Docker e caminho separado para desenvolvimento do frontend.

## User Experience

A experiencia comeca no README. O usuario deve conseguir escolher entre subir tudo com Docker ou rodar o frontend separadamente durante desenvolvimento. Ao abrir o frontend, ele ve o Painel do Laboratorio Keycloak com uma chamada clara para entrar pelo Keycloak.

Depois do login, a interface mostra quem e o usuario atual e quais permissoes efetivas estao disponiveis. A jornada guiada apresenta passos curtos que explicam a relacao entre usuario, permissao, acao, endpoint e resposta. O usuario deve conseguir completar essa trilha sem precisar abrir o Postman.

Na area de documentos, o usuario pode operar a aplicacao de forma livre. O `leitor` consegue ler documentos, mas ve criacao, edicao e aprovacao bloqueadas com explicacao. O `editor` consegue ler, criar e editar, mas nao aprovar. O `aprovador` consegue ler e aprovar, mas nao criar ou editar se nao tiver essas permissoes.

A interface deve evitar excesso de texto. Explicacoes devem ser curtas, visiveis no momento certo e conectadas a acoes reais. `401` e `403` devem ser explicados de forma didatica, mas o MVP nao deve centralizar a experiencia na visualizacao de token ou claims.

## High-Level Technical Constraints

- O frontend deve viver em `frontend/`, mantendo o projeto como monolito modular com modulos separados por responsabilidade.
- O produto deve usar login real via Keycloak e nao simulacao de usuario.
- O frontend deve usar um client publico proprio no Keycloak, separado do client confidential da API.
- Nenhum client secret deve ser exigido ou exposto no navegador.
- A experiencia deve ser compativel com os usuarios e permissoes atuais do laboratorio: `leitor`, `editor`, `aprovador` e permissoes de `documentos`.
- A experiencia completa deve poder subir junto com Keycloak e backend por Docker.
- Deve existir caminho documentado para rodar somente o frontend em modo de desenvolvimento.
- O frontend deve seguir PT-BR-first em codigo e interface quando fizer sentido.

## Non-Goals

- Criar um admin completo do Keycloak.
- Editar realm, policies, scopes ou permissoes pela interface.
- Construir um gerenciador documental rico com filtros avancados, anexos, historico ou workflow complexo.
- Substituir totalmente o Postman como ferramenta de estudo avancado.
- Exibir token bruto como foco principal da experiencia.
- Criar internacionalizacao no MVP.
- Criar persistencia real de documentos.
- Ensinar hardening de producao, TLS ou gestao real de segredos.

## Phased Rollout Plan

### MVP (Phase 1)

- Frontend em `frontend/`.
- Login e logout reais via Keycloak.
- Jornada guiada conectando usuario, permissoes, endpoint, acao e resposta.
- Area de documentos com listar, detalhe, criar, editar e aprovar.
- Acoes bloqueadas com explicacao da permissao ausente.
- Painel de permissoes efetivas.
- Diagnostico simples da ultima acao relevante.
- README com comandos para subir tudo e rodar frontend separadamente.

Success criteria to proceed:

- Usuario completa a jornada guiada em ate 5 minutos.
- Todos os perfis principais demonstram pelo menos uma acao permitida e uma acao bloqueada.
- O fluxo completo funciona sem abrir Postman.
- A experiencia mantem PT-BR em telas e codigo do frontend.

### Phase 2

- Melhorar roteiro guiado com desafios curtos.
- Adicionar uma matriz visual usuario x acao x permissao x resultado.
- Adicionar explicacoes melhores para casos `401` e `403`.
- Melhorar estado vazio, mensagens de erro e recuperacao de sessao.

Success criteria to proceed:

- Usuario consegue prever o resultado de uma acao antes de executa-la.
- Demo cobre os tres usuarios de estudo com clareza.
- Usuario consegue explicar diferenca entre autenticacao e autorizacao usando a tela.

### Phase 3

- Expandir material de apoio integrado ao frontend.
- Adicionar modo de apresentacao para aulas ou gravacoes.
- Integrar links contextuais para docs locais do projeto.
- Evoluir desafios para exercicios de alteracao de permissao no Keycloak.

Success criteria:

- O painel vira uma trilha de estudo reutilizavel.
- O frontend complementa Postman e documentacao sem esconder conceitos centrais.
- O laboratorio continua simples de subir e explicar.

## Success Metrics

- **Tempo de jornada guiada:** <= 5 minutos para concluir login, visualizar permissoes e observar uma acao permitida e uma bloqueada.
- **Cobertura de permissoes:** 4 permissoes demonstradas: `documentos:ler`, `documentos:criar`, `documentos:editar`, `documentos:aprovar`.
- **Cobertura de perfis:** 3 usuarios de estudo demonstraveis: `leitor`, `editor`, `aprovador`.
- **Clareza de acoes bloqueadas:** 100% das acoes indisponiveis mostram a permissao ausente.
- **Cobertura funcional:** listar, detalhe, criar, editar e aprovar disponiveis na experiencia.
- **Consistencia PT-BR:** >= 90% de aderencia em interface, mensagens, nomes de arquivos, funcoes, componentes e variaveis do frontend.
- **Execucao local:** experiencia completa sobe com um comando Docker documentado.

## Risks and Mitigations

- **Risco: o frontend esconder a autorizacao.**  
  Mitigacao: manter permissoes, acao, endpoint e status visiveis nas interacoes relevantes.

- **Risco: virar gerenciador documental completo.**  
  Mitigacao: limitar documentos ao necessario para exercitar permissoes.

- **Risco: a jornada guiada ficar longa ou cansativa.**  
  Mitigacao: manter passos curtos e permitir uso livre apos a trilha.

- **Risco: o usuario confundir botao desabilitado com seguranca real.**  
  Mitigacao: explicar que a interface ajuda o aprendizado, mas o backend continua validando permissoes.

- **Risco: expor detalhes sensiveis ou ensinar padrao inseguro.**  
  Mitigacao: evitar secret no navegador e nao fazer do token bruto o foco visual.

- **Risco: o padrao PT-BR no codigo ficar inconsistente.**  
  Mitigacao: incluir consistencia PT-BR como criterio explicito de sucesso.

## Architecture Decision Records

- [ADR-001: Escopo da V1 como Painel Didatico de Autorizacao](adrs/adr-001.md) — Define a V1 como painel didatico, com documentos reais, diagnostico HTTP e client SPA proprio.
- [ADR-002: Abordagem do MVP com Modo Guiado e Uso Livre](adrs/adr-002.md) — Define o MVP com jornada guiada e uso livre de documentos.

## Open Questions

- Qual stack de frontend sera escolhida na TechSpec?
- O frontend usara JavaScript ou TypeScript?
- Quais textos exatos compoem a primeira jornada guiada?
- A tela inicial deve comecar em modo guiado automaticamente ou oferecer escolha entre guiado e uso livre?
- Quais documentos iniciais devem aparecer na demo para facilitar aprovacao, edicao e leitura?
