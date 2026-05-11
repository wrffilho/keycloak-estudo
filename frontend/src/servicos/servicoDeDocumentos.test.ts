import { beforeEach, describe, expect, it, vi } from 'vitest';
import { listarDocumentos, criarDocumento, editarDocumento, aprovarDocumento } from './servicoDeDocumentos';
import { requisitar } from './clienteHttp';
import { obterRpt } from './servicoDeAutenticacao';

vi.mock('./clienteHttp', () => ({
  requisitar: vi.fn()
}));

vi.mock('./servicoDeAutenticacao', () => ({
  obterRpt: vi.fn()
}));

describe('servicoDeDocumentos', () => {
  beforeEach(() => {
    vi.mocked(obterRpt).mockResolvedValue('rpt-documentos');
    vi.mocked(requisitar).mockResolvedValue([]);
  });

  it('lista documentos usando RPT de leitura', async () => {
    await listarDocumentos();

    expect(obterRpt).toHaveBeenCalledWith('documentos#ler');
    expect(requisitar).toHaveBeenCalledWith('/documentos', {
      token: 'rpt-documentos'
    });
  });

  it('cria documento usando RPT de criacao', async () => {
    await criarDocumento({ titulo: 'Novo', conteudo: 'Conteudo' });

    expect(obterRpt).toHaveBeenCalledWith('documentos#criar');
    expect(requisitar).toHaveBeenCalledWith('/documentos', {
      metodo: 'POST',
      corpo: { titulo: 'Novo', conteudo: 'Conteudo' },
      token: 'rpt-documentos'
    });
  });

  it('edita documento usando RPT de edicao', async () => {
    await editarDocumento('doc-001', { titulo: 'Atualizado', conteudo: 'Conteudo atualizado' });

    expect(obterRpt).toHaveBeenCalledWith('documentos#editar');
    expect(requisitar).toHaveBeenCalledWith('/documentos/doc-001', {
      metodo: 'PUT',
      corpo: { titulo: 'Atualizado', conteudo: 'Conteudo atualizado' },
      token: 'rpt-documentos'
    });
  });

  it('aprova documento usando RPT de aprovacao', async () => {
    await aprovarDocumento('doc-001');

    expect(obterRpt).toHaveBeenCalledWith('documentos#aprovar');
    expect(requisitar).toHaveBeenCalledWith('/documentos/doc-001/aprovar', {
      metodo: 'POST',
      token: 'rpt-documentos'
    });
  });
});
