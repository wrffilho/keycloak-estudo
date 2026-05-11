package br.com.wanderlei.keycloakestudo.configuracao;

import java.util.List;
import java.util.Map;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.EnforcementMode;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.MethodConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.PathConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.ScopeEnforcementMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnProperty(prefix = "laboratorio.keycloak.policy-enforcer", name = "enabled", havingValue = "true")
public class PolicyEnforcerConfig {

    @Bean
    FilterRegistrationBean<ServletPolicyEnforcerFilter> filtroPolicyEnforcer(
            @Value("${laboratorio.keycloak.policy-enforcer.auth-server-url}") String authServerUrl,
            @Value("${laboratorio.keycloak.policy-enforcer.realm}") String realm,
            @Value("${laboratorio.keycloak.policy-enforcer.resource}") String resource,
            @Value("${laboratorio.keycloak.policy-enforcer.secret}") String secret) {

        org.keycloak.representations.adapters.config.PolicyEnforcerConfig config =
                criarConfiguracaoPolicyEnforcer(authServerUrl, realm, resource, secret);

        ServletPolicyEnforcerFilter filtro = new ServletPolicyEnforcerFilter(request -> config);

        FilterRegistrationBean<ServletPolicyEnforcerFilter> registration = new FilterRegistrationBean<>(filtro);
        registration.setName("filtroPolicyEnforcerKeycloak");
        registration.addUrlPatterns("/documentos", "/documentos/*");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    static org.keycloak.representations.adapters.config.PolicyEnforcerConfig criarConfiguracaoPolicyEnforcer(
            String authServerUrl,
            String realm,
            String resource,
            String secret) {

        org.keycloak.representations.adapters.config.PolicyEnforcerConfig config =
                new org.keycloak.representations.adapters.config.PolicyEnforcerConfig();
        config.setAuthServerUrl(authServerUrl);
        config.setRealm(realm);
        config.setResource(resource);
        config.setCredentials(Map.of("secret", secret));
        config.setEnforcementMode(EnforcementMode.ENFORCING);
        config.setLazyLoadPaths(false);
        config.setPaths(List.of(
                caminho("/documentos", metodo("GET", "ler"), metodo("POST", "criar")),
                caminho("/documentos/{id}", metodo("GET", "ler"), metodo("PUT", "editar")),
                caminho("/documentos/{id}/aprovar", metodo("POST", "aprovar"))
        ));
        return config;
    }

    private static PathConfig caminho(String path, MethodConfig... methods) {
        PathConfig config = new PathConfig();
        config.setName("documentos");
        config.setPath(path);
        config.setMethods(List.of(methods));
        config.setEnforcementMode(EnforcementMode.ENFORCING);
        return config;
    }

    private static MethodConfig metodo(String method, String scope) {
        MethodConfig config = new MethodConfig();
        config.setMethod(method);
        config.setScopes(List.of(scope));
        config.setScopesEnforcementMode(ScopeEnforcementMode.ALL);
        return config;
    }
}
