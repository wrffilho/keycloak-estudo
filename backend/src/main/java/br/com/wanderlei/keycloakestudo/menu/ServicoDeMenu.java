package br.com.wanderlei.keycloakestudo.menu;

import java.util.List;

public interface ServicoDeMenu {

    List<ItemDeMenuResposta> listarMenuDoUsuario(String nomeDoUsuario);
}
