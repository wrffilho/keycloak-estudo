package br.com.wanderlei.keycloakestudo.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ServicoDeMenuKeycloakTest {

    @Test
    void montaArvoreOrdenadaFiltrandoPorRolesDoUsuario() {
        ClienteKeycloakMenu cliente = new ClienteFake(
                List.of(
                        item("operacoes", "Operacoes", "/tela/operacoes", "menu", 20, null, "menu:operacoes"),
                        item("cadastros", "Cadastros", "/tela/cadastros", "menu", 10, null, "menu:cadastros"),
                        item("clientes", "Clientes", "/tela/clientes", "submenu", 20, "cadastros", "submenu:clientes"),
                        item("fornecedores", "Fornecedores", "/tela/fornecedores", "submenu", 10, "cadastros", "submenu:fornecedores"),
                        item("usuarios", "Usuarios", "/tela/usuarios", "submenu", 10, "administracao", "submenu:usuarios")
                ),
                Set.of("menu:cadastros", "submenu:clientes", "submenu:fornecedores")
        );

        List<ItemDeMenuResposta> menu = new ServicoDeMenuKeycloak(cliente).listarMenuDoUsuario("editor");

        assertThat(menu).extracting(ItemDeMenuResposta::id).containsExactly("cadastros");
        assertThat(menu.getFirst().filhos())
                .extracting(ItemDeMenuResposta::id)
                .containsExactly("fornecedores", "clientes");
    }

    @Test
    void ignoraItensInvalidos() {
        ClienteKeycloakMenu cliente = new ClienteFake(
                List.of(new ItemDeMenuCatalogo("sem-rota", "Sem rota", "", "menu", 10, null, null, "menu:sem-rota")),
                Set.of("menu:sem-rota")
        );

        List<ItemDeMenuResposta> menu = new ServicoDeMenuKeycloak(cliente).listarMenuDoUsuario("editor");

        assertThat(menu).isEmpty();
    }

    private ItemDeMenuCatalogo item(String id, String rotulo, String rota, String tipo, Integer ordem, String pai,
                                    String roleNecessaria) {
        return new ItemDeMenuCatalogo(id, rotulo, rota, tipo, ordem, null, pai, roleNecessaria);
    }

    private record ClienteFake(List<ItemDeMenuCatalogo> itens, Set<String> roles) implements ClienteKeycloakMenu {

        @Override
        public List<ItemDeMenuCatalogo> listarItensDeMenu() {
            return itens;
        }

        @Override
        public Set<String> listarRolesDoUsuario(String nomeDoUsuario) {
            return roles;
        }
    }
}
