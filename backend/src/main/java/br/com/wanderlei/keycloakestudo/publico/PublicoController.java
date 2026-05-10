package br.com.wanderlei.keycloakestudo.publico;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publico")
public class PublicoController {

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of(
                "status", "online",
                "horario", OffsetDateTime.now().toString()
        );
    }

    @GetMapping("/sobre")
    public Map<String, String> sobre() {
        return Map.of(
                "nome", "Laboratorio de Autorizacao com Keycloak",
                "objetivo", "Aprender endpoints publicos, autenticados e autorizados em PT-BR."
        );
    }
}
