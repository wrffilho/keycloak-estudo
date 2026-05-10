package br.com.wanderlei.keycloakestudo.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EndpointsWebTest {

    @Autowired
    private MockMvc mockMvc;

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
    void documentosRetornaProibidoSemPermissao() throws Exception {
        mockMvc.perform(get("/documentos").with(jwt()))
                .andExpect(status().isForbidden());
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
