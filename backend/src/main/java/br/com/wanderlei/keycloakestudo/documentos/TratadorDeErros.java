package br.com.wanderlei.keycloakestudo.documentos;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(DocumentoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> documentoNaoEncontrado(DocumentoNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> requisicaoInvalida() {
        return ResponseEntity.badRequest()
                .body(Map.of("erro", "Requisicao invalida. Informe titulo e conteudo."));
    }
}
