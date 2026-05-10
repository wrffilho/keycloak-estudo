package br.com.wanderlei.keycloakestudo.seguranca;

import java.util.Set;
import java.util.TreeSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LeitorDePermissoesSpringSecurity implements LeitorDePermissoes {

    @Override
    public boolean possuiPermissao(String nomeDaPermissao) {
        return listarPermissoes().contains(nomeDaPermissao);
    }

    @Override
    public Set<String> listarPermissoes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Set.of();
        }
        Set<String> permissoes = new TreeSet<>();
        authentication.getAuthorities().forEach(authority -> {
            String valor = authority.getAuthority();
            if (valor.startsWith("SCOPE_")) {
                permissoes.add(valor.substring("SCOPE_".length()));
            } else if (valor.contains(":")) {
                permissoes.add(valor);
            }
        });
        return permissoes;
    }
}
