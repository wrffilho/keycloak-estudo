import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ErroHttp, requisitar } from './clienteHttp';
import { obterToken } from './servicoDeAutenticacao';

vi.mock('./servicoDeAutenticacao', () => ({
  obterToken: vi.fn()
}));

describe('clienteHttp', () => {
  beforeEach(() => {
    vi.mocked(obterToken).mockResolvedValue('token-teste');
    vi.stubGlobal('fetch', vi.fn());
  });

  it('envia token, corpo em JSON e devolve a resposta da API', async () => {
    vi.mocked(fetch).mockResolvedValue({
      ok: true,
      status: 200,
      json: () => Promise.resolve({ id: '1', titulo: 'Contrato' })
    } as Response);

    const resposta = await requisitar('/documentos', {
      metodo: 'POST',
      corpo: { titulo: 'Contrato' }
    });

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8081/documentos',
      expect.objectContaining({
        method: 'POST',
        headers: {
          Authorization: 'Bearer token-teste',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ titulo: 'Contrato' })
      })
    );
    expect(resposta).toEqual({ id: '1', titulo: 'Contrato' });
  });

  it('lança ErroHttp com mensagem retornada pelo backend', async () => {
    vi.mocked(fetch).mockResolvedValue({
      ok: false,
      status: 403,
      json: () => Promise.resolve({ erro: 'Sem permissao' })
    } as Response);

    await expect(requisitar('/documentos')).rejects.toEqual(new ErroHttp(403, 'Sem permissao'));
  });

});
