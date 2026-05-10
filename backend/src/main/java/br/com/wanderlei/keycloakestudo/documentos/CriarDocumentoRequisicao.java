package br.com.wanderlei.keycloakestudo.documentos;

import jakarta.validation.constraints.NotBlank;

public record CriarDocumentoRequisicao(
        @NotBlank String titulo,
        @NotBlank String conteudo
) {
}
