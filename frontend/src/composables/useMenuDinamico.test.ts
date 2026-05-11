import { beforeEach, describe, expect, it, vi } from 'vitest';
import { useMenuDinamico } from './useMenuDinamico';
import { buscarMenu } from '../servicos/servicoDeMenu';

vi.mock('../servicos/servicoDeMenu', () => ({
  buscarMenu: vi.fn()
}));

describe('useMenuDinamico', () => {
  beforeEach(() => {
    vi.mocked(buscarMenu).mockReset();
    useMenuDinamico().limparMenu();
  });

  it('carrega menu e encontra item aninhado por rota', async () => {
    vi.mocked(buscarMenu).mockResolvedValue({
      itens: [
        {
          id: 'cadastros',
          rotulo: 'Cadastros',
          rota: '/tela/cadastros',
          tipo: 'menu',
          ordem: 10,
          filhos: [
            {
              id: 'clientes',
              rotulo: 'Clientes',
              rota: '/tela/cadastros-clientes',
              tipo: 'submenu',
              ordem: 10,
              filhos: []
            }
          ]
        }
      ]
    });

    const menu = useMenuDinamico();
    await menu.carregarMenu();

    expect(menu.itensDeMenu.value).toHaveLength(1);
    expect(menu.buscarItemPorRota('/tela/cadastros-clientes')?.rotulo).toBe('Clientes');
    expect(menu.buscarItemPorRota('/tela/inexistente')).toBeUndefined();
  });

  it('limpa menu e registra erro quando a API falha', async () => {
    vi.mocked(buscarMenu).mockRejectedValue(new Error('falha'));

    const menu = useMenuDinamico();
    await menu.carregarMenu();

    expect(menu.itensDeMenu.value).toEqual([]);
    expect(menu.erroMenu.value).toBe('Nao foi possivel carregar o menu do usuario.');
  });
});
