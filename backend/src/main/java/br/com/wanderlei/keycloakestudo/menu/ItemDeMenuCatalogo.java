package br.com.wanderlei.keycloakestudo.menu;

public record ItemDeMenuCatalogo(
        String id,
        String rotulo,
        String rota,
        String tipo,
        Integer ordem,
        String icone,
        String pai,
        String roleNecessaria
) {

    public boolean valido() {
        return textoPreenchido(id)
                && textoPreenchido(rotulo)
                && textoPreenchido(rota)
                && textoPreenchido(tipo)
                && ordem != null
                && textoPreenchido(roleNecessaria);
    }

    public boolean possuiPai() {
        return textoPreenchido(pai);
    }

    private boolean textoPreenchido(String valor) {
        return valor != null && !valor.isBlank();
    }
}
