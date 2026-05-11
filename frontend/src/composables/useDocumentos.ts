import { ref } from 'vue';
import {
  aprovarDocumento,
  buscarDocumentoPorId,
  criarDocumento,
  editarDocumento,
  listarDocumentos
} from '../servicos/servicoDeDocumentos';
import type { CriarDocumento, Documento, EditarDocumento } from '../tipos/documento';
import { useDiagnosticoDeAcao } from './useDiagnosticoDeAcao';

const documentos = ref<Documento[]>([]);
const documentoSelecionado = ref<Documento | undefined>();
const carregandoDocumentos = ref(false);
const erroDocumentos = ref<string | undefined>();

export function useDocumentos() {
  const { registrarErro, registrarSucesso } = useDiagnosticoDeAcao();

  async function carregarDocumentos() {
    carregandoDocumentos.value = true;
    erroDocumentos.value = undefined;

    try {
      documentos.value = await listarDocumentos();
      registrarSucesso('Listar documentos', 'GET', '/documentos');
    } catch (erro) {
      erroDocumentos.value = 'Nao foi possivel carregar documentos.';
      registrarErro('Listar documentos', 'GET', '/documentos', erro);
    } finally {
      carregandoDocumentos.value = false;
    }
  }

  async function carregarDocumento(id: string) {
    carregandoDocumentos.value = true;
    erroDocumentos.value = undefined;

    try {
      documentoSelecionado.value = await buscarDocumentoPorId(id);
      registrarSucesso('Buscar documento', 'GET', `/documentos/${id}`);
    } catch (erro) {
      erroDocumentos.value = 'Nao foi possivel carregar o documento.';
      registrarErro('Buscar documento', 'GET', `/documentos/${id}`, erro);
    } finally {
      carregandoDocumentos.value = false;
    }
  }

  async function criar(novoDocumento: CriarDocumento) {
    try {
      const criado = await criarDocumento(novoDocumento);
      documentos.value = [criado, ...documentos.value];
      registrarSucesso('Criar documento', 'POST', '/documentos', 201);
      return criado;
    } catch (erro) {
      registrarErro('Criar documento', 'POST', '/documentos', erro);
      throw erro;
    }
  }

  async function editar(id: string, documento: EditarDocumento) {
    try {
      const editado = await editarDocumento(id, documento);
      documentoSelecionado.value = editado;
      documentos.value = documentos.value.map((item) => (item.id === id ? editado : item));
      registrarSucesso('Editar documento', 'PUT', `/documentos/${id}`);
      return editado;
    } catch (erro) {
      registrarErro('Editar documento', 'PUT', `/documentos/${id}`, erro);
      throw erro;
    }
  }

  async function aprovar(id: string) {
    try {
      const aprovado = await aprovarDocumento(id);
      documentoSelecionado.value = aprovado;
      documentos.value = documentos.value.map((item) => (item.id === id ? aprovado : item));
      registrarSucesso('Aprovar documento', 'POST', `/documentos/${id}/aprovar`);
      return aprovado;
    } catch (erro) {
      registrarErro('Aprovar documento', 'POST', `/documentos/${id}/aprovar`, erro);
      throw erro;
    }
  }

  return {
    documentos,
    documentoSelecionado,
    carregandoDocumentos,
    erroDocumentos,
    carregarDocumentos,
    carregarDocumento,
    criar,
    editar,
    aprovar
  };
}
