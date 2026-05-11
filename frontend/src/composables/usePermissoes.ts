import { computed, ref } from 'vue';
import { buscarPerfil, buscarPermissoes } from '../servicos/servicoDeUsuario';
import type { PerfilUsuario, Permissao } from '../tipos/seguranca';

const perfil = ref<PerfilUsuario | undefined>();
const permissoes = ref<Permissao[]>([]);
const carregandoPermissoes = ref(false);
const erroPermissoes = ref<string | undefined>();

export function usePermissoes() {
  async function carregarPermissoes() {
    carregandoPermissoes.value = true;
    erroPermissoes.value = undefined;

    try {
      const [perfilResposta, permissoesResposta] = await Promise.all([buscarPerfil(), buscarPermissoes()]);
      perfil.value = perfilResposta;
      permissoes.value = permissoesResposta.permissoes;
    } catch {
      erroPermissoes.value = 'Nao foi possivel carregar perfil e permissoes.';
      perfil.value = undefined;
      permissoes.value = [];
    } finally {
      carregandoPermissoes.value = false;
    }
  }

  function possuiPermissao(permissao: Permissao) {
    return permissoes.value.includes(permissao);
  }

  function mensagemPermissaoAusente(permissao: Permissao) {
    return `Falta a permissao ${permissao}.`;
  }

  return {
    perfil,
    permissoes: computed(() => permissoes.value),
    carregandoPermissoes,
    erroPermissoes,
    carregarPermissoes,
    possuiPermissao,
    mensagemPermissaoAusente,
    podeLerDocumentos: computed(() => possuiPermissao('documentos:ler')),
    podeCriarDocumento: computed(() => possuiPermissao('documentos:criar')),
    podeEditarDocumento: computed(() => possuiPermissao('documentos:editar')),
    podeAprovarDocumento: computed(() => possuiPermissao('documentos:aprovar'))
  };
}
