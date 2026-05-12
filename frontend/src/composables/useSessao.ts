import { computed, ref } from 'vue';
import {
  entrar,
  estaAutenticado,
  inicializarAutenticacao,
  obterUsuario,
  sair
} from '../servicos/servicoDeAutenticacao';

const carregandoSessao = ref(false);
const autenticado = ref(false);
const usuario = ref<string | undefined>();
const erroSessao = ref<string | undefined>();
let promessaDeInicializacao: Promise<void> | null = null;

export function useSessao() {
  async function inicializarSessao() {
    if (promessaDeInicializacao !== null) {
      return promessaDeInicializacao;
    }

    promessaDeInicializacao = executarInicializacaoSessao();
    return promessaDeInicializacao;
  }

  async function executarInicializacaoSessao() {
    carregandoSessao.value = true;
    erroSessao.value = undefined;

    try {
      autenticado.value = await inicializarAutenticacao();
      usuario.value = obterUsuario();
    } catch {
      erroSessao.value = 'Nao foi possivel iniciar a sessao com o Keycloak.';
      autenticado.value = estaAutenticado();
      usuario.value = obterUsuario();
    } finally {
      carregandoSessao.value = false;
    }
  }

  return {
    carregandoSessao,
    autenticado: computed(() => autenticado.value),
    usuario: computed(() => usuario.value),
    erroSessao,
    inicializarSessao,
    entrar,
    sair
  };
}
