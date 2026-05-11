import { describe, expect, it } from 'vitest';
import { useDiagnosticoDeAcao } from './useDiagnosticoDeAcao';
import { ErroHttp } from '../servicos/clienteHttp';

describe('useDiagnosticoDeAcao', () => {
  it('registra sucesso com metodo, endpoint e status retornado pela API', () => {
    const { diagnostico, registrarSucesso } = useDiagnosticoDeAcao();

    registrarSucesso('Listar documentos', 'GET', '/documentos', 200);

    expect(diagnostico.value).toMatchObject({
      acao: 'Listar documentos',
      metodo: 'GET',
      endpoint: '/documentos',
      status: 200,
      permitido: true
    });
  });

  it('registra bloqueio feito pela interface quando falta permissao', () => {
    const { diagnostico, registrarBloqueio } = useDiagnosticoDeAcao();

    registrarBloqueio('Aprovar documento', 'POST', '/documentos/1/aprovar', 'documentos:aprovar');

    expect(diagnostico.value.permitido).toBe(false);
    expect(diagnostico.value.status).toBe(403);
    expect(diagnostico.value.mensagem).toContain('documentos:aprovar');
  });

  it('traduz erros 401 e 403 para mensagens didaticas', () => {
    const { diagnostico, registrarErro } = useDiagnosticoDeAcao();

    registrarErro('Criar documento', 'POST', '/documentos', new ErroHttp(401, 'Nao autorizado'));
    expect(diagnostico.value.mensagem).toContain('401');

    registrarErro('Editar documento', 'PUT', '/documentos/1', new ErroHttp(403, 'Proibido'));
    expect(diagnostico.value.mensagem).toContain('403');
  });
});
