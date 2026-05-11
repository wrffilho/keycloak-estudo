package br.com.wanderlei.keycloakestudo.configuracao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PolicyEnforcerConfigTest {

    @Test
    void configuraCaminhosMetodosEEscoposDeDocumentos() {
        var config = PolicyEnforcerConfig.criarConfiguracaoPolicyEnforcer(
                "http://localhost:8080",
                "laboratorio-keycloak",
                "laboratorio-api",
                "segredo");

        assertThat(config.getAuthServerUrl()).isEqualTo("http://localhost:8080");
        assertThat(config.getRealm()).isEqualTo("laboratorio-keycloak");
        assertThat(config.getResource()).isEqualTo("laboratorio-api");
        assertThat(config.getCredentials()).containsEntry("secret", "segredo");

        assertThat(config.getPaths()).hasSize(3);
        assertThat(config.getPaths().get(0).getPath()).isEqualTo("/documentos");
        assertThat(config.getPaths().get(0).getMethods())
                .extracting(method -> method.getMethod() + ":" + method.getScopes().getFirst())
                .containsExactly("GET:ler", "POST:criar");
        assertThat(config.getPaths().get(1).getPath()).isEqualTo("/documentos/{id}");
        assertThat(config.getPaths().get(1).getMethods())
                .extracting(method -> method.getMethod() + ":" + method.getScopes().getFirst())
                .containsExactly("GET:ler", "PUT:editar");
        assertThat(config.getPaths().get(2).getPath()).isEqualTo("/documentos/{id}/aprovar");
        assertThat(config.getPaths().get(2).getMethods())
                .extracting(method -> method.getMethod() + ":" + method.getScopes().getFirst())
                .containsExactly("POST:aprovar");
    }
}
