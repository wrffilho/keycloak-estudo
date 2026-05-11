package br.com.wanderlei.keycloakestudo.documentos;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documentos")
public class DocumentosController {

    private final ServicoDeDocumentos servico;

    public DocumentosController(ServicoDeDocumentos servico) {
        this.servico = servico;
    }

    @GetMapping
    public List<DocumentoResposta> listar() {
        return servico.listar();
    }

    @GetMapping("/{id}")
    public DocumentoResposta buscarPorId(@PathVariable String id) {
        return servico.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<DocumentoResposta> criar(@Valid @RequestBody CriarDocumentoRequisicao requisicao) {
        DocumentoResposta resposta = servico.criar(requisicao);
        return ResponseEntity.created(URI.create("/documentos/" + resposta.id())).body(resposta);
    }

    @PutMapping("/{id}")
    public DocumentoResposta editar(@PathVariable String id, @Valid @RequestBody EditarDocumentoRequisicao requisicao) {
        return servico.editar(id, requisicao);
    }

    @PostMapping("/{id}/aprovar")
    public DocumentoResposta aprovar(@PathVariable String id) {
        return servico.aprovar(id);
    }
}
