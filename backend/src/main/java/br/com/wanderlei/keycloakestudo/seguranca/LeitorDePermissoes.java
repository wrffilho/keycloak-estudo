package br.com.wanderlei.keycloakestudo.seguranca;

import java.util.Set;

public interface LeitorDePermissoes {

    boolean possuiPermissao(String nomeDaPermissao);

    Set<String> listarPermissoes();
}
