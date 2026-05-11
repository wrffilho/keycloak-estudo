package br.com.wanderlei.keycloakestudo.menu;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "laboratorio.keycloak.menu")
public record KeycloakMenuProperties(
        String url,
        String realm,
        String clientId,
        String adminClientId,
        String adminClientSecret
) {

    public String baseUrl() {
        return semBarraFinal(url);
    }

    private String semBarraFinal(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }
        return valor.endsWith("/") ? valor.substring(0, valor.length() - 1) : valor;
    }
}
