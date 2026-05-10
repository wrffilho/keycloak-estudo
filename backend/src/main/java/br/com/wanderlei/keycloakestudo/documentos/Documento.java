package br.com.wanderlei.keycloakestudo.documentos;

public class Documento {

    private final String id;
    private String titulo;
    private String conteudo;
    private StatusDocumento status;
    private final String criadoPor;

    public Documento(String id, String titulo, String conteudo, StatusDocumento status, String criadoPor) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.status = status;
        this.criadoPor = criadoPor;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public StatusDocumento getStatus() {
        return status;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void editar(String novoTitulo, String novoConteudo) {
        this.titulo = novoTitulo;
        this.conteudo = novoConteudo;
        this.status = StatusDocumento.PUBLICADO;
    }

    public void aprovar() {
        this.status = StatusDocumento.APROVADO;
    }
}
