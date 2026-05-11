package br.com.wanderlei.keycloakestudo.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class ServicoDeMenuKeycloak implements ServicoDeMenu {

    private final ClienteKeycloakMenu clienteKeycloakMenu;

    public ServicoDeMenuKeycloak(ClienteKeycloakMenu clienteKeycloakMenu) {
        this.clienteKeycloakMenu = clienteKeycloakMenu;
    }

    @Override
    public List<ItemDeMenuResposta> listarMenuDoUsuario(String nomeDoUsuario) {
        Set<String> roles = clienteKeycloakMenu.listarRolesDoUsuario(nomeDoUsuario);
        List<ItemDeMenuCatalogo> itensPermitidos = clienteKeycloakMenu.listarItensDeMenu().stream()
                .filter(ItemDeMenuCatalogo::valido)
                .filter(item -> roles.contains(item.roleNecessaria()))
                .sorted(Comparator.comparing(ItemDeMenuCatalogo::ordem).thenComparing(ItemDeMenuCatalogo::rotulo))
                .toList();

        return montarArvore(itensPermitidos);
    }

    private List<ItemDeMenuResposta> montarArvore(List<ItemDeMenuCatalogo> itens) {
        Map<String, List<ItemDeMenuCatalogo>> filhosPorPai = new LinkedHashMap<>();
        itens.stream()
                .filter(ItemDeMenuCatalogo::possuiPai)
                .forEach(item -> filhosPorPai.computeIfAbsent(item.pai(), ignored -> new ArrayList<>()).add(item));

        return itens.stream()
                .filter(item -> !item.possuiPai())
                .map(item -> converter(item, filhosPorPai))
                .toList();
    }

    private ItemDeMenuResposta converter(ItemDeMenuCatalogo item, Map<String, List<ItemDeMenuCatalogo>> filhosPorPai) {
        List<ItemDeMenuResposta> filhos = filhosPorPai.getOrDefault(item.id(), List.of()).stream()
                .sorted(Comparator.comparing(ItemDeMenuCatalogo::ordem).thenComparing(ItemDeMenuCatalogo::rotulo))
                .map(filho -> converter(filho, filhosPorPai))
                .toList();

        return new ItemDeMenuResposta(
                item.id(),
                item.rotulo(),
                item.rota(),
                item.tipo(),
                item.ordem(),
                item.icone(),
                item.roleNecessaria(),
                filhos
        );
    }
}
