package br.com.wanderlei.keycloakestudo.configuracao;

import java.util.List;
import java.util.Map;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.EnforcementMode;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.MethodConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.PathConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig.ScopeEnforcementMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@ConditionalOnProperty(prefix = "laboratorio.keycloak.policy-enforcer", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(PolicyEnforcerProperties.class)
public class PolicyEnforcerConfig {

    @Bean
    FilterRegistrationBean<ServletPolicyEnforcerFilter> filtroPolicyEnforcer(PolicyEnforcerProperties properties) {
        org.keycloak.representations.adapters.config.PolicyEnforcerConfig config = criarConfiguracao(properties);

        ServletPolicyEnforcerFilter filtro = new ServletPolicyEnforcerFilter(request -> config);

        String[] urlPatterns = properties.paths().stream()
                .map(PolicyEnforcerProperties.PathProperties::path)
                .distinct()
                .toArray(String[]::new);

        FilterRegistrationBean<ServletPolicyEnforcerFilter> registration = new FilterRegistrationBean<>(filtro);
        registration.setName("filtroPolicyEnforcerKeycloak");
        registration.addUrlPatterns(urlPatterns);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    static org.keycloak.representations.adapters.config.PolicyEnforcerConfig criarConfiguracao(
            PolicyEnforcerProperties properties) {

        org.keycloak.representations.adapters.config.PolicyEnforcerConfig config =
                new org.keycloak.representations.adapters.config.PolicyEnforcerConfig();
        config.setAuthServerUrl(properties.authServerUrl());
        config.setRealm(properties.realm());
        config.setResource(properties.resource());
        config.setCredentials(Map.of("secret", properties.secret()));
        config.setEnforcementMode(EnforcementMode.ENFORCING);
        config.setLazyLoadPaths(true);
        config.setPaths(properties.paths().stream().map(PolicyEnforcerConfig::converterPath).toList());
        return config;
    }

    private static PathConfig converterPath(PolicyEnforcerProperties.PathProperties p) {
        PathConfig config = new PathConfig();
        config.setName(p.resource());
        config.setPath(p.path());
        config.setEnforcementMode(EnforcementMode.ENFORCING);
        config.setMethods(p.methods().stream().map(PolicyEnforcerConfig::converterMetodo).toList());
        return config;
    }

    private static MethodConfig converterMetodo(PolicyEnforcerProperties.MethodProperties m) {
        MethodConfig config = new MethodConfig();
        config.setMethod(m.method());
        config.setScopes(List.of(m.scope()));
        config.setScopesEnforcementMode(ScopeEnforcementMode.ALL);
        return config;
    }
}
