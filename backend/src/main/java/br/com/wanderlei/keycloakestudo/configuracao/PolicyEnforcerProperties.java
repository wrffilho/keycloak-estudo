package br.com.wanderlei.keycloakestudo.configuracao;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "laboratorio.keycloak.policy-enforcer")
public record PolicyEnforcerProperties(
        String authServerUrl,
        String realm,
        String resource,
        String secret,
        List<PathProperties> paths
) {

    public record PathProperties(
            String resource,
            String path,
            List<MethodProperties> methods
    ) {}

    public record MethodProperties(String method, String scope) {}
}
