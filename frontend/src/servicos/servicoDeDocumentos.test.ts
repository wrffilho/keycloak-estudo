import { beforeEach, describe, expect, it, vi } from 'vitest';
import { listarDocumentos, criarDocumento, editarDocumento, aprovarDocumento } from './servicoDeDocumentos';
import { requisitar } from './clienteHttp';

vi.mock('./clienteHttp', () => ({
  requisitar: vi.fn()
}));

describe('servicoDeDocumentos', () => {
  beforeEach(() => {
    vi.mocked(requisitar).mockResolvedValue([]);
  });

  it('lista documentos usando access token comum do cliente HTTP', async () => {
    await listarDocumentos();

    expect(requisitar).toHaveBeenCalledWith('/documentos');
  });

  it('cria documento sem conhecer RPT no frontend', async () => {
    await criarDocumento({ titulo: 'Novo', conteudo: 'Conteudo' });

    expect(requisitar).toHaveBeenCalledWith('/documentos', {
      metodo: 'POST',
      corpo: { titulo: 'Novo', conteudo: 'Conteudo' }
    });
  });

  it('edita documento sem conhecer RPT no frontend', async () => {
    await editarDocumento('doc-001', { titulo: 'Atualizado', conteudo: 'Conteudo atualizado' });

    expect(requisitar).toHaveBeenCalledWith('/documentos/doc-001', {
      metodo: 'PUT',
      corpo: { titulo: 'Atualizado', conteudo: 'Conteudo atualizado' }
    });
  });

  it('aprova documento sem conhecer RPT no frontend', async () => {
    await aprovarDocumento('doc-001');

    expect(requisitar).toHaveBeenCalledWith('/documentos/doc-001/aprovar', {
      metodo: 'POST'
    });
  });
});
