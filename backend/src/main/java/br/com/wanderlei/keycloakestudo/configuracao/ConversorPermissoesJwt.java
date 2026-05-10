package br.com.wanderlei.keycloakestudo.configuracao;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class ConversorPermissoesJwt implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String clientId;

    public ConversorPermissoesJwt(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        adicionarScopes(jwt, authorities);
        adicionarRolesDoRealm(jwt, authorities);
        adicionarRolesDoClient(jwt, authorities);
        adicionarPermissoesDeAutorizacao(jwt, authorities);
        String nome = jwt.getClaimAsString("preferred_username");
        return new JwtAuthenticationToken(jwt, authorities, nome == null ? jwt.getSubject() : nome);
    }

    private void adicionarScopes(Jwt jwt, Set<GrantedAuthority> authorities) {
        String scope = jwt.getClaimAsString("scope");
        if (scope != null) {
            for (String item : scope.split(" ")) {
                adicionarAuthority(authorities, item);
            }
        }
        List<String> scp = jwt.getClaimAsStringList("scp");
        if (scp != null) {
            scp.forEach(item -> adicionarAuthority(authorities, item));
        }
    }

    @SuppressWarnings("unchecked")
    private void adicionarRolesDoRealm(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return;
        }
        Object roles = realmAccess.get("roles");
        if (roles instanceof Collection<?> collection) {
            collection.forEach(role -> adicionarAuthority(authorities, String.valueOf(role)));
        }
    }

    @SuppressWarnings("unchecked")
    private void adicionarRolesDoClient(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return;
        }
        Object client = resourceAccess.get(clientId);
        if (client instanceof Map<?, ?> clientMap) {
            Object roles = clientMap.get("roles");
            if (roles instanceof Collection<?> collection) {
                collection.forEach(role -> adicionarAuthority(authorities, String.valueOf(role)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void adicionarPermissoesDeAutorizacao(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> authorization = jwt.getClaim("authorization");
        if (authorization == null) {
            return;
        }
        Object permissions = authorization.get("permissions");
        if (!(permissions instanceof Collection<?> collection)) {
            return;
        }
        for (Object permission : collection) {
            if (permission instanceof Map<?, ?> permissionMap) {
                Object scopes = permissionMap.get("scopes");
                if (scopes instanceof Collection<?> scopeCollection) {
                    scopeCollection.forEach(scope -> adicionarAuthority(authorities, "documentos:" + scope));
                }
            }
        }
    }

    private void adicionarAuthority(Set<GrantedAuthority> authorities, String valor) {
        if (valor == null || valor.isBlank()) {
            return;
        }
        authorities.add(new SimpleGrantedAuthority(valor));
        authorities.add(new SimpleGrantedAuthority("SCOPE_" + valor));
    }
}
