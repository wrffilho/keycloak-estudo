import { beforeEach, describe, expect, it, vi } from 'vitest';
import { usePermissoes } from './usePermissoes';
import { buscarPerfil, buscarPermissoes } from '../servicos/servicoDeUsuario';

vi.mock('../servicos/servicoDeUsuario', () => ({
  buscarPerfil: vi.fn(),
  buscarPermissoes: vi.fn()
}));

describe('usePermissoes', () => {
  beforeEach(() => {
    vi.mocked(buscarPerfil).mockReset();
    vi.mocked(buscarPermissoes).mockReset();
  });

  it('carrega perfil e calcula permissoes de documentos', async () => {
    vi.mocked(buscarPerfil).mockResolvedValue({
      usuario: 'maria',
      autenticado: true
    });
    vi.mocked(buscarPermissoes).mockResolvedValue({
      permissoes: ['documentos:ler', 'documentos:criar']
    });

    const permissoes = usePermissoes();
    await permissoes.carregarPermissoes();

    expect(permissoes.perfil.value?.usuario).toBe('maria');
    expect(permissoes.podeLerDocumentos.value).toBe(true);
    expect(permissoes.podeCriarDocumento.value).toBe(true);
    expect(permissoes.podeEditarDocumento.value).toBe(false);
    expect(permissoes.mensagemPermissaoAusente('documentos:editar')).toContain('documentos:editar');
  });

  it('limpa estado e mostra erro quando a API de usuario falha', async () => {
    vi.mocked(buscarPerfil).mockRejectedValue(new Error('falha'));
    vi.mocked(buscarPermissoes).mockResolvedValue({
      permissoes: ['documentos:ler']
    });

    const permissoes = usePermissoes();
    await permissoes.carregarPermissoes();

    expect(permissoes.permissoes.value).toEqual([]);
    expect(permissoes.erroPermissoes.value).toBe('Nao foi possivel carregar perfil e permissoes.');
  });
});
