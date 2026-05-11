package br.com.wanderlei.keycloakestudo.seguranca;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wanderlei.keycloakestudo.menu.RespostaMenu;
import br.com.wanderlei.keycloakestudo.menu.ServicoDeMenu;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final LeitorDePermissoes leitorDePermissoes;
    private final ServicoDeMenu servicoDeMenu;

    public UsuarioController(LeitorDePermissoes leitorDePermissoes, ServicoDeMenu servicoDeMenu) {
        this.leitorDePermissoes = leitorDePermissoes;
        this.servicoDeMenu = servicoDeMenu;
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

    @GetMapping("/menu")
    public RespostaMenu menu(Authentication authentication) {
        return new RespostaMenu(servicoDeMenu.listarMenuDoUsuario(authentication.getName()));
    }
}
