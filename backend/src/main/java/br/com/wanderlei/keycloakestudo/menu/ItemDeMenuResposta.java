package br.com.wanderlei.keycloakestudo.menu;

import java.util.List;

public record ItemDeMenuResposta(
        String id,
        String rotulo,
        String rota,
        String tipo,
        Integer ordem,
        String icone,
        String roleNecessaria,
        List<ItemDeMenuResposta> filhos
) {
}
