# Glossario rapido

- **Autenticacao**: prova quem e o usuario. No laboratorio, acontece quando voce obtem um token no Keycloak.
- **Autorizacao**: decide o que o usuario pode fazer. No laboratorio, aparece quando um endpoint exige uma permissao como `documentos:ler`.
- **Token**: texto emitido pelo Keycloak que a API valida antes de liberar endpoints protegidos.
- **Recurso**: aquilo que sera protegido. Aqui usamos o recurso `documentos`.
- **Escopo**: acao permitida sobre um recurso, como `ler`, `criar`, `editar` e `aprovar`.
- **Politica**: regra que diz quem pode receber acesso.
- **Permissao**: ligacao entre recurso, escopo e politica.
- **401**: voce nao esta autenticado ou enviou token invalido.
- **403**: voce esta autenticado, mas nao tem permissao para aquela acao.
