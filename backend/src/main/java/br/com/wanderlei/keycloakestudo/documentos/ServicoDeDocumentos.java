package br.com.wanderlei.keycloakestudo.documentos;

import java.util.List;

public interface ServicoDeDocumentos {

    List<DocumentoResposta> listar();

    DocumentoResposta buscarPorId(String id);

    DocumentoResposta criar(CriarDocumentoRequisicao requisicao);

    DocumentoResposta editar(String id, EditarDocumentoRequisicao requisicao);

    DocumentoResposta aprovar(String id);
}
