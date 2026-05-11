package br.com.wanderlei.keycloakestudo.menu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ItemDeMenuCatalogoTest {

    @Test
    void itemComAtributosObrigatoriosEValido() {
        ItemDeMenuCatalogo item = new ItemDeMenuCatalogo(
                "cadastros",
                "Cadastros",
                "/tela/cadastros",
                "menu",
                10,
                "Folder",
                null,
                "menu:cadastros"
        );

        assertThat(item.valido()).isTrue();
        assertThat(item.possuiPai()).isFalse();
    }

    @Test
    void itemSemRoleNecessariaEInvalido() {
        ItemDeMenuCatalogo item = new ItemDeMenuCatalogo(
                "cadastros",
                "Cadastros",
                "/tela/cadastros",
                "menu",
                10,
                null,
                null,
                " "
        );

        assertThat(item.valido()).isFalse();
    }
}
