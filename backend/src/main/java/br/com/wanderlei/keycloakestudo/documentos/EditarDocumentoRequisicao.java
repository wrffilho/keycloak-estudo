package br.com.wanderlei.keycloakestudo.documentos;

import jakarta.validation.constraints.NotBlank;

public record EditarDocumentoRequisicao(
        @NotBlank String titulo,
        @NotBlank String conteudo
) {
}
