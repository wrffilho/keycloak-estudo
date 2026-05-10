package br.com.wanderlei.keycloakestudo.seguranca;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import br.com.wanderlei.keycloakestudo.configuracao.ConversorPermissoesJwt;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class ConversorPermissoesJwtTest {

    private final ConversorPermissoesJwt conversor = new ConversorPermissoesJwt("laboratorio-api");

    @Test
    void converteScopesRolesEPermissoesEmAuthorities() {
        Jwt jwt = jwt(Map.of(
                "preferred_username", "ana",
                "scope", "documentos:ler",
                "realm_access", Map.of("roles", List.of("leitor")),
                "resource_access", Map.of("laboratorio-api", Map.of("roles", List.of("documentos:editar"))),
                "authorization", Map.of("permissions", List.of(Map.of("rsname", "documentos", "scopes", List.of("aprovar"))))
        ));

        var authorities = conversor.convert(jwt).getAuthorities();

        assertThat(authorities).extracting("authority")
                .contains("documentos:ler", "SCOPE_documentos:ler", "leitor", "documentos:editar", "documentos:aprovar");
    }

    @Test
    void usaSubjectQuandoUsernameNaoExiste() {
        Jwt jwt = jwt(Map.of("scope", "documentos:ler"));

        assertThat(conversor.convert(jwt).getName()).isEqualTo("subject-1");
    }

    private Jwt jwt(Map<String, Object> claims) {
        return new Jwt("token", Instant.now(), Instant.now().plusSeconds(600),
                Map.of("alg", "none"), withSubject(claims));
    }

    private Map<String, Object> withSubject(Map<String, Object> claims) {
        java.util.LinkedHashMap<String, Object> copy = new java.util.LinkedHashMap<>(claims);
        copy.put("sub", "subject-1");
        return copy;
    }
}
