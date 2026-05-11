package br.com.wanderlei.keycloakestudo.menu;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ClienteKeycloakMenuAdminRest implements ClienteKeycloakMenu {

    private static final ParameterizedTypeReference<List<Map<String, Object>>> LISTA_DE_MAPAS =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;
    private final KeycloakMenuProperties properties;

    public ClienteKeycloakMenuAdminRest(RestClient.Builder builder, KeycloakMenuProperties properties) {
        this.restClient = builder.build();
        this.properties = properties;
    }

    @Override
    public List<ItemDeMenuCatalogo> listarItensDeMenu() {
        String token = obterTokenAdministrativo();
        String clientUuid = buscarClientUuid(token);
        try {
            List<Map<String, Object>> resources = restClient.get()
                    .uri("%s/admin/realms/%s/clients/%s/authz/resource-server/resource?type=frontend-menu"
                            .formatted(properties.baseUrl(), properties.realm(), clientUuid))
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .body(LISTA_DE_MAPAS);

            if (resources == null) {
                return List.of();
            }

            return resources.stream()
                    .map(this::converterResource)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (RestClientException exception) {
            throw new ErroCatalogoDeMenuException("Nao foi possivel carregar o catalogo de menu no Keycloak.", exception);
        }
    }

    @Override
    public Set<String> listarRolesDoUsuario(String nomeDoUsuario) {
        String token = obterTokenAdministrativo();
        String clientUuid = buscarClientUuid(token);
        String usuarioId = buscarUsuarioId(token, nomeDoUsuario);
        try {
            List<Map<String, Object>> roles = restClient.get()
                    .uri("%s/admin/realms/%s/users/%s/role-mappings/clients/%s/composite"
                            .formatted(properties.baseUrl(), properties.realm(), usuarioId, clientUuid))
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .body(LISTA_DE_MAPAS);

            Set<String> nomes = new TreeSet<>();
            if (roles != null) {
                roles.forEach(role -> {
                    Object nome = role.get("name");
                    if (nome != null && !String.valueOf(nome).isBlank()) {
                        nomes.add(String.valueOf(nome));
                    }
                });
            }
            return nomes;
        } catch (RestClientException exception) {
            throw new ErroCatalogoDeMenuException("Nao foi possivel carregar as roles de menu do usuario.", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private ItemDeMenuCatalogo converterResource(Map<String, Object> resource) {
        Object attributesObject = resource.get("attributes");
        if (!(attributesObject instanceof Map<?, ?> attributes)) {
            return null;
        }

        return new ItemDeMenuCatalogo(
                primeiroAtributo((Map<String, Object>) attributes, "id"),
                primeiroAtributo((Map<String, Object>) attributes, "rotulo"),
                primeiroAtributo((Map<String, Object>) attributes, "rota"),
                primeiroAtributo((Map<String, Object>) attributes, "tipo"),
                inteiro(primeiroAtributo((Map<String, Object>) attributes, "ordem")),
                primeiroAtributo((Map<String, Object>) attributes, "icone"),
                primeiroAtributo((Map<String, Object>) attributes, "pai"),
                primeiroAtributo((Map<String, Object>) attributes, "roleNecessaria")
        );
    }

    private String obterTokenAdministrativo() {
        try {
            var form = new LinkedMultiValueMap<String, String>();
            form.add("grant_type", "client_credentials");
            form.add("client_id", properties.adminClientId());
            form.add("client_secret", properties.adminClientSecret());

            Map<String, Object> resposta = restClient.post()
                    .uri("%s/realms/%s/protocol/openid-connect/token".formatted(properties.baseUrl(), properties.realm()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            Object token = resposta == null ? null : resposta.get("access_token");
            if (token == null || String.valueOf(token).isBlank()) {
                throw new ErroCatalogoDeMenuException("Keycloak nao retornou token administrativo.");
            }
            return String.valueOf(token);
        } catch (RestClientException exception) {
            throw new ErroCatalogoDeMenuException("Nao foi possivel autenticar no Keycloak para ler menus.", exception);
        }
    }

    private String buscarClientUuid(String token) {
        try {
            List<Map<String, Object>> clients = restClient.get()
                    .uri("%s/admin/realms/%s/clients?clientId=%s"
                            .formatted(properties.baseUrl(), properties.realm(), properties.clientId()))
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .body(LISTA_DE_MAPAS);

            if (clients == null || clients.isEmpty()) {
                throw new ErroCatalogoDeMenuException("Client de menu nao encontrado no Keycloak.");
            }
            return String.valueOf(clients.getFirst().get("id"));
        } catch (RestClientException exception) {
            throw new ErroCatalogoDeMenuException("Nao foi possivel localizar o client de menu no Keycloak.", exception);
        }
    }

    private String buscarUsuarioId(String token, String nomeDoUsuario) {
        try {
            List<Map<String, Object>> usuarios = restClient.get()
                    .uri("%s/admin/realms/%s/users?username=%s&exact=true"
                            .formatted(properties.baseUrl(), properties.realm(), nomeDoUsuario))
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .body(LISTA_DE_MAPAS);

            if (usuarios == null || usuarios.isEmpty()) {
                throw new ErroCatalogoDeMenuException("Usuario nao encontrado no Keycloak.");
            }
            return String.valueOf(usuarios.getFirst().get("id"));
        } catch (RestClientException exception) {
            throw new ErroCatalogoDeMenuException("Nao foi possivel localizar o usuario no Keycloak.", exception);
        }
    }

    private String primeiroAtributo(Map<String, Object> attributes, String nome) {
        Object valor = attributes.get(nome);
        if (valor instanceof List<?> lista && !lista.isEmpty()) {
            Object primeiro = lista.getFirst();
            return primeiro == null ? null : String.valueOf(primeiro);
        }
        if (valor == null) {
            return null;
        }
        return String.valueOf(valor);
    }

    private Integer inteiro(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return Integer.valueOf(valor);
    }
}
