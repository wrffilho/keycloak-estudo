package br.com.wanderlei.keycloakestudo.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import br.com.wanderlei.keycloakestudo.menu.ClienteKeycloakMenu;
import br.com.wanderlei.keycloakestudo.menu.ItemDeMenuCatalogo;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(properties = "laboratorio.keycloak.policy-enforcer.enabled=false")
@AutoConfigureMockMvc
class EndpointsWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteKeycloakMenu clienteKeycloakMenu;

    @Test
    void publicoStatusRetornaOkSemToken() throws Exception {
        mockMvc.perform(get("/publico/status"))
                .andExpect(status().isOk());
    }

    @Test
    void perfilRetornaNaoAutorizadoSemToken() throws Exception {
        mockMvc.perform(get("/usuario/perfil"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void perfilRetornaOkComJwt() throws Exception {
        mockMvc.perform(get("/usuario/perfil").with(jwt().jwt(jwt -> jwt.claim("preferred_username", "leitor"))))
                .andExpect(status().isOk());
    }

    @Test
    void menuRetornaNaoAutorizadoSemToken() throws Exception {
        mockMvc.perform(get("/usuario/menu"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void menuRetornaItensPermitidosComJwt() throws Exception {
        when(clienteKeycloakMenu.listarRolesDoUsuario(anyString())).thenReturn(Set.of("menu:cadastros"));
        when(clienteKeycloakMenu.listarItensDeMenu()).thenReturn(List.of(
                new ItemDeMenuCatalogo(
                        "cadastros",
                        "Cadastros",
                        "/tela/cadastros",
                        "menu",
                        10,
                        "Folder",
                        null,
                        "menu:cadastros"
                )
        ));

        mockMvc.perform(get("/usuario/menu").with(jwt().jwt(jwt -> jwt.claim("preferred_username", "editor"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].rotulo").value("Cadastros"));
    }

    @Test
    void documentosRetornaOkComJwtQuandoPolicyEnforcerEstaDesabilitadoNoTeste() throws Exception {
        mockMvc.perform(get("/documentos").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void documentosRetornaOkComPermissaoDeLeitura() throws Exception {
        mockMvc.perform(get("/documentos").with(jwt()
                        .authorities(new SimpleGrantedAuthority("documentos:ler"))))
                .andExpect(status().isOk());
    }

    @Test
    void criarDocumentoRetornaCriadoComPermissaoDeCriacao() throws Exception {
        mockMvc.perform(post("/documentos")
                        .with(jwt().authorities(new SimpleGrantedAuthority("documentos:criar")))
                        .contentType("application/json")
                        .content("{\"titulo\":\"Novo\",\"conteudo\":\"Conteudo\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void editarDocumentoRetornaOkComPermissaoDeEdicao() throws Exception {
        mockMvc.perform(put("/documentos/doc-001")
                        .with(jwt().authorities(new SimpleGrantedAuthority("documentos:editar")))
                        .contentType("application/json")
                        .content("{\"titulo\":\"Atualizado\",\"conteudo\":\"Conteudo atualizado\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void aprovarDocumentoRetornaOkComPermissaoDeAprovacao() throws Exception {
        mockMvc.perform(post("/documentos/doc-002/aprovar")
                        .with(jwt().authorities(new SimpleGrantedAuthority("documentos:aprovar"))))
                .andExpect(status().isOk());
    }
}
