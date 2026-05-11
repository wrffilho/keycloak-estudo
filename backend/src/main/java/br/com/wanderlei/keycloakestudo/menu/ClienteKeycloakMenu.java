package br.com.wanderlei.keycloakestudo.menu;

import java.util.List;
import java.util.Set;

public interface ClienteKeycloakMenu {

    List<ItemDeMenuCatalogo> listarItensDeMenu();

    Set<String> listarRolesDoUsuario(String nomeDoUsuario);
}
