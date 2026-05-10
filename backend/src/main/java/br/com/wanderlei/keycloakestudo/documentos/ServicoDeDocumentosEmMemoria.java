package br.com.wanderlei.keycloakestudo.documentos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ServicoDeDocumentosEmMemoria implements ServicoDeDocumentos {

    private final Map<String, Documento> documentos = new LinkedHashMap<>();

    public ServicoDeDocumentosEmMemoria() {
        documentos.put("doc-001", new Documento("doc-001", "Politica de ferias",
                "Regras internas para solicitacao de ferias.", StatusDocumento.PUBLICADO, "editor"));
        documentos.put("doc-002", new Documento("doc-002", "Contrato de fornecedor",
                "Documento pendente de aprovacao.", StatusDocumento.RASCUNHO, "editor"));
        documentos.put("doc-003", new Documento("doc-003", "Relatorio de auditoria",
                "Resumo de auditoria interna.", StatusDocumento.PUBLICADO, "aprovador"));
    }

    @Override
    public List<DocumentoResposta> listar() {
        return documentos.values().stream()
                .map(DocumentoResposta::de)
                .toList();
    }

    @Override
    public DocumentoResposta buscarPorId(String id) {
        return DocumentoResposta.de(obter(id));
    }

    @Override
    public DocumentoResposta criar(CriarDocumentoRequisicao requisicao) {
        String id = "doc-" + UUID.randomUUID();
        Documento documento = new Documento(id, requisicao.titulo(), requisicao.conteudo(),
                StatusDocumento.RASCUNHO, "usuario-logado");
        documentos.put(id, documento);
        return DocumentoResposta.de(documento);
    }

    @Override
    public DocumentoResposta editar(String id, EditarDocumentoRequisicao requisicao) {
        Documento documento = obter(id);
        documento.editar(requisicao.titulo(), requisicao.conteudo());
        return DocumentoResposta.de(documento);
    }

    @Override
    public DocumentoResposta aprovar(String id) {
        Documento documento = obter(id);
        documento.aprovar();
        return DocumentoResposta.de(documento);
    }

    private Documento obter(String id) {
        Documento documento = documentos.get(id);
        if (documento == null) {
            throw new DocumentoNaoEncontradoException(id);
        }
        return documento;
    }
}
