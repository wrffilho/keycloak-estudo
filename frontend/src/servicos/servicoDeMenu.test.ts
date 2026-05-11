import { describe, expect, it, vi } from 'vitest';
import { buscarMenu } from './servicoDeMenu';
import { requisitar } from './clienteHttp';

vi.mock('./clienteHttp', () => ({
  requisitar: vi.fn()
}));

describe('servicoDeMenu', () => {
  it('busca menu do usuario no endpoint autenticado', async () => {
    vi.mocked(requisitar).mockResolvedValue({
      itens: [
        {
          id: 'cadastros',
          rotulo: 'Cadastros',
          rota: '/tela/cadastros',
          tipo: 'menu',
          ordem: 10,
          filhos: []
        }
      ]
    });

    const resposta = await buscarMenu();

    expect(requisitar).toHaveBeenCalledWith('/usuario/menu');
    expect(resposta.itens[0].rotulo).toBe('Cadastros');
  });
});
