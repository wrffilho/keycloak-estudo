package br.com.wanderlei.keycloakestudo.documentos;

public class DocumentoNaoEncontradoException extends RuntimeException {

    public DocumentoNaoEncontradoException(String id) {
        super("Documento nao encontrado: " + id);
    }
}
