package br.com.wanderlei.keycloakestudo.seguranca;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final LeitorDePermissoes leitorDePermissoes;

    public UsuarioController(LeitorDePermissoes leitorDePermissoes) {
        this.leitorDePermissoes = leitorDePermissoes;
    }

    @GetMapping("/perfil")
    public Map<String, Object> perfil(Authentication authentication) {
        return Map.of(
                "usuario", authentication.getName(),
                "autenticado", authentication.isAuthenticated()
        );
    }

    @GetMapping("/permissoes")
    public Map<String, Object> permissoes() {
        return Map.of("permissoes", leitorDePermissoes.listarPermissoes());
    }
}
