import { mount } from '@vue/test-utils';
import { describe, expect, it, vi } from 'vitest';
import TelaFicticia from './TelaFicticia.vue';

const buscarItemPorRota = vi.fn();
const exibirAcessoNegado = vi.fn();

vi.mock('vue-router', () => ({
  useRoute: () => ({ path: '/tela/cadastros-clientes' })
}));

vi.mock('../composables/useSessao', () => ({
  useSessao: () => ({
    usuario: 'editor'
  })
}));

vi.mock('../composables/useMenuDinamico', () => ({
  useMenuDinamico: () => ({
    garantirMenuCarregado: vi.fn(),
    buscarItemPorRota
  })
}));

vi.mock('../composables/useAlertaDeAcessoNegado', () => ({
  useAlertaDeAcessoNegado: () => ({
    exibirAcessoNegado
  })
}));

describe('TelaFicticia', () => {
  it('mostra mensagem didatica quando a rota esta no menu', () => {
    buscarItemPorRota.mockReturnValue({
      id: 'clientes',
      rotulo: 'Clientes',
      rota: '/tela/cadastros-clientes',
      tipo: 'submenu',
      ordem: 10,
      filhos: []
    });

    const wrapper = mount(TelaFicticia);

    expect(wrapper.text()).toContain('Usuario editor acessou Clientes porque o menu foi liberado pelo Keycloak.');
  });

  it('exibe alerta de acesso negado quando a rota nao esta no menu', async () => {
    buscarItemPorRota.mockReturnValue(undefined);

    mount(TelaFicticia);
    await Promise.resolve();

    expect(exibirAcessoNegado).toHaveBeenCalledWith('Acesso negado. Seu usuario nao tem acesso a esta tela.');
  });
});
