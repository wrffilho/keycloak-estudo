import { computed, ref } from 'vue';
import { buscarMenu } from '../servicos/servicoDeMenu';
import type { ItemDeMenu } from '../tipos/menu';

const itens = ref<ItemDeMenu[]>([]);
const carregandoMenu = ref(false);
const menuCarregado = ref(false);
const erroMenu = ref<string | undefined>();

export function useMenuDinamico() {
  async function carregarMenu() {
    carregandoMenu.value = true;
    erroMenu.value = undefined;

    try {
      const resposta = await buscarMenu();
      itens.value = resposta.itens;
      menuCarregado.value = true;
    } catch {
      itens.value = [];
      menuCarregado.value = false;
      erroMenu.value = 'Nao foi possivel carregar o menu do usuario.';
    } finally {
      carregandoMenu.value = false;
    }
  }

  async function garantirMenuCarregado() {
    if (!menuCarregado.value && !carregandoMenu.value) {
      await carregarMenu();
    }
  }

  function limparMenu() {
    itens.value = [];
    erroMenu.value = undefined;
    menuCarregado.value = false;
  }

  function buscarItemPorRota(rota: string) {
    return achatarItens(itens.value).find((item) => item.rota === rota);
  }

  function buscarItemPorId(id: string) {
    return achatarItens(itens.value).find((item) => item.id === id);
  }

  return {
    itensDeMenu: computed(() => itens.value),
    itensDeMenuAchatados: computed(() => achatarItens(itens.value)),
    carregandoMenu,
    menuCarregado: computed(() => menuCarregado.value),
    erroMenu,
    carregarMenu,
    garantirMenuCarregado,
    limparMenu,
    buscarItemPorRota,
    buscarItemPorId
  };
}

function achatarItens(lista: ItemDeMenu[]): ItemDeMenu[] {
  return lista.flatMap((item) => [item, ...achatarItens(item.filhos ?? [])]);
}
