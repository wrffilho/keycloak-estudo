package br.com.wanderlei.keycloakestudo.configuracao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class PolicyEnforcerConfigTest {

    @Test
    void configuraCaminhosMetodosEEscoposDeDocumentos() {
        var properties = propriedadesPolicyEnforcer();

        var config = PolicyEnforcerConfig.criarConfiguracao(properties);

        assertThat(config.getAuthServerUrl()).isEqualTo("http://localhost:8080");
        assertThat(config.getRealm()).isEqualTo("laboratorio-keycloak");
        assertThat(config.getResource()).isEqualTo("laboratorio-api");
        assertThat(config.getCredentials()).containsEntry("secret", "segredo");

        assertThat(config.getPaths()).hasSize(2);
        assertThat(config.getPaths().get(0).getPath()).isEqualTo("/documentos");
        assertThat(config.getPaths().get(0).getMethods())
                .extracting(m -> m.getMethod() + ":" + m.getScopes().getFirst())
                .containsExactly("GET:ler", "POST:criar");
        assertThat(config.getPaths().get(1).getPath()).isEqualTo("/documentos/*");
        assertThat(config.getPaths().get(1).getMethods())
                .extracting(m -> m.getMethod() + ":" + m.getScopes().getFirst())
                .containsExactly("GET:ler", "PUT:editar", "POST:aprovar");
    }

    @Test
    void registraFiltroNosPatternsConfigurados() {
        var config = new PolicyEnforcerConfig();

        var registration = config.filtroPolicyEnforcer(propriedadesPolicyEnforcer());

        assertThat(registration.getFilter()).isNotNull();
        assertThat(registration.getFilterName()).isEqualTo("filtroPolicyEnforcerKeycloak");
        assertThat(registration.getUrlPatterns()).containsExactly("/documentos", "/documentos/*");
    }

    private static PolicyEnforcerProperties propriedadesPolicyEnforcer() {
        return new PolicyEnforcerProperties(
                "http://localhost:8080",
                "laboratorio-keycloak",
                "laboratorio-api",
                "segredo",
                List.of(
                        new PolicyEnforcerProperties.PathProperties("documentos", "/documentos", List.of(
                                new PolicyEnforcerProperties.MethodProperties("GET", "ler"),
                                new PolicyEnforcerProperties.MethodProperties("POST", "criar")
                        )),
                        new PolicyEnforcerProperties.PathProperties("documentos", "/documentos/*", List.of(
                                new PolicyEnforcerProperties.MethodProperties("GET", "ler"),
                                new PolicyEnforcerProperties.MethodProperties("PUT", "editar"),
                                new PolicyEnforcerProperties.MethodProperties("POST", "aprovar")
                        ))
                )
        );
    }
}
