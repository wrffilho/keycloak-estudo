package br.com.wanderlei.keycloakestudo.documentos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ServicoDeDocumentosEmMemoriaTest {

    private final ServicoDeDocumentosEmMemoria servico = new ServicoDeDocumentosEmMemoria();

    @Test
    void listarRetornaDocumentosIniciais() {
        assertThat(servico.listar()).hasSize(3);
    }

    @Test
    void buscarDocumentoExistente() {
        DocumentoResposta resposta = servico.buscarPorId("doc-001");

        assertThat(resposta.titulo()).isEqualTo("Politica de ferias");
    }

    @Test
    void buscarDocumentoInexistenteFalha() {
        assertThatThrownBy(() -> servico.buscarPorId("nao-existe"))
                .isInstanceOf(DocumentoNaoEncontradoException.class);
    }

    @Test
    void criarDocumentoEmRascunho() {
        DocumentoResposta resposta = servico.criar(new CriarDocumentoRequisicao("Novo", "Conteudo"));

        assertThat(resposta.id()).startsWith("doc-");
        assertThat(resposta.status()).isEqualTo("RASCUNHO");
    }

    @Test
    void editarDocumentoPublicaAlteracao() {
        DocumentoResposta resposta = servico.editar("doc-002",
                new EditarDocumentoRequisicao("Contrato revisado", "Texto revisado"));

        assertThat(resposta.titulo()).isEqualTo("Contrato revisado");
        assertThat(resposta.status()).isEqualTo("PUBLICADO");
    }

    @Test
    void aprovarDocumento() {
        DocumentoResposta resposta = servico.aprovar("doc-002");

        assertThat(resposta.status()).isEqualTo("APROVADO");
    }
}
