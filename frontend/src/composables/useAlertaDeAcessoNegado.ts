import { computed, ref } from 'vue';

const visivel = ref(false);
const mensagem = ref('');

export function useAlertaDeAcessoNegado() {
  function exibirAcessoNegado(texto: string) {
    mensagem.value = texto;
    visivel.value = true;
  }

  function fecharAcessoNegado() {
    visivel.value = false;
  }

  return {
    alertaAcessoNegadoVisivel: computed(() => visivel.value),
    mensagemAcessoNegado: computed(() => mensagem.value),
    exibirAcessoNegado,
    fecharAcessoNegado
  };
}
