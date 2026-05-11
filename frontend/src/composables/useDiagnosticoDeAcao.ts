import { ref } from 'vue';
import { ErroHttp } from '../servicos/clienteHttp';
import type { DiagnosticoDeAcao, MetodoHttp } from '../tipos/diagnostico';

const diagnostico = ref<DiagnosticoDeAcao>({
  acao: 'Aguardando acao',
  metodo: 'GET',
  endpoint: '-',
  mensagem: 'Entre com um usuario do laboratorio e execute uma acao.',
  permitido: true
});

export function useDiagnosticoDeAcao() {
  function registrarSucesso(acao: string, metodo: MetodoHttp, endpoint: string, status = 200) {
    diagnostico.value = {
      acao,
      metodo,
      endpoint,
      status,
      permitido: true,
      mensagem: `Acao concluida. O backend retornou ${status}.`
    };
  }

  function registrarBloqueio(acao: string, metodo: MetodoHttp, endpoint: string, permissao: string) {
    diagnostico.value = {
      acao,
      metodo,
      endpoint,
      status: 403,
      permitido: false,
      mensagem: `Acao bloqueada na interface. Falta a permissao ${permissao}.`
    };
  }

  function registrarErro(acao: string, metodo: MetodoHttp, endpoint: string, erro: unknown) {
    const status = erro instanceof ErroHttp ? erro.status : undefined;
    diagnostico.value = {
      acao,
      metodo,
      endpoint,
      status,
      permitido: false,
      mensagem: mensagemParaErro(status, erro)
    };
  }

  return {
    diagnostico,
    registrarSucesso,
    registrarBloqueio,
    registrarErro
  };
}

function mensagemParaErro(status: number | undefined, erro: unknown) {
  if (status === 401) {
    return 'A API retornou 401. O usuario nao esta autenticado ou a sessao expirou.';
  }

  if (status === 403) {
    return 'A API retornou 403. O usuario esta autenticado, mas nao tem permissao para esta acao.';
  }

  if (erro instanceof Error) {
    return erro.message;
  }

  return 'Nao foi possivel concluir a acao.';
}
