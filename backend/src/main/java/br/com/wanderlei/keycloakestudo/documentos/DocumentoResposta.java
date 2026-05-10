package br.com.wanderlei.keycloakestudo.documentos;

public record DocumentoResposta(
        String id,
        String titulo,
        String conteudo,
        String status
) {
    static DocumentoResposta de(Documento documento) {
        return new DocumentoResposta(
                documento.getId(),
                documento.getTitulo(),
                documento.getConteudo(),
                documento.getStatus().name()
        );
    }
}
