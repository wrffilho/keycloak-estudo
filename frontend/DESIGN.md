# Diretriz de design do frontend

Este painel existe para estudar autorizacao com Keycloak com baixa carga cognitiva. A interface deve parecer uma ferramenta de laboratorio: clara, direta, previsivel e escrita em portugues do Brasil.

## Principios

- Mostrar o estado atual antes da acao: usuario, autenticacao e permissoes visiveis.
- Explicar bloqueios sem esconder a regra: quando faltar permissao, mostrar qual permissao faltou.
- Separar tentativa da resposta: a pessoa clica, o painel registra se a interface bloqueou ou se a API retornou `401`, `403`, `200` ou `201`.
- Usar rotas reais com Vue Router para simular um uso normal de aplicacao.
- Manter textos, nomes de componentes, composables, servicos e funcoes em PT-BR sempre que isso nao prejudicar bibliotecas ou configuracoes.

## Visual

- Base clara, com branco, cinza frio, azul discreto e estados vermelhos/verdes somente quando houver significado.
- Layout de ferramenta, nao landing page: cabecalho fixo, navegacao curta, paineis de leitura e area principal de trabalho.
- Bordas leves, raio pequeno e espacamento consistente.
- Tipografia do sistema para evitar dependencia visual extra.
- Nada de decoracao que concorra com o aprendizado.

## Componentes esperados

- Cabecalho com navegacao entre laboratorio e documentos.
- Painel de sessao com usuario autenticado e acao de entrar/sair.
- Painel de permissoes com as permissoes carregadas da API.
- Painel de diagnostico com metodo, endpoint, status e mensagem.
- Botoes com permissao que ficam desabilitados quando a acao nao pode ser feita.
- Formularios simples para criar e editar documentos.

## Estados importantes

- Carregando sessao.
- Nao autenticado.
- Autenticado sem permissao.
- Acao permitida e concluida.
- Bloqueio pela interface.
- Erro retornado pelo backend.

## Acessibilidade

- Botoes devem ter texto claro.
- Estados desabilitados devem ter explicacao proxima.
- Cores nao devem ser a unica forma de entendimento.
- Textos precisam caber em telas pequenas.
