<script setup lang="ts">
import { onMounted } from 'vue';
import { RouterLink, useRoute } from 'vue-router';
import BotaoComPermissao from '../componentes/documentos/BotaoComPermissao.vue';
import FormularioDocumento from '../componentes/documentos/FormularioDocumento.vue';
import PainelDeDiagnostico from '../componentes/laboratorio/PainelDeDiagnostico.vue';
import { useDiagnosticoDeAcao } from '../composables/useDiagnosticoDeAcao';
import { useDocumentos } from '../composables/useDocumentos';
import { usePermissoes } from '../composables/usePermissoes';
import type { EditarDocumento } from '../tipos/documento';

const route = useRoute();
const id = String(route.params.id);

const { documentoSelecionado, carregandoDocumentos, erroDocumentos, carregarDocumento, editar, aprovar } =
  useDocumentos();
const { podeEditarDocumento, podeAprovarDocumento, mensagemPermissaoAusente, carregarPermissoes } =
  usePermissoes();
const { diagnostico, registrarBloqueio } = useDiagnosticoDeAcao();

onMounted(async () => {
  await carregarPermissoes();
  await carregarDocumento(id);
});

async function editarAtualizado(documento: EditarDocumento) {
  if (!podeEditarDocumento.value) {
    registrarBloqueio('Editar documento', 'PUT', `/documentos/${id}`, 'documentos:editar');
    return;
  }
  await editar(id, documento);
}

async function aprovarAtual() {
  if (!podeAprovarDocumento.value) {
    registrarBloqueio('Aprovar documento', 'POST', `/documentos/${id}/aprovar`, 'documentos:aprovar');
    return;
  }
  await aprovar(id);
}

function classeBadge(status: string) {
  return (
    { RASCUNHO: 'badge-rascunho', PUBLICADO: 'badge-publicado', APROVADO: 'badge-aprovado' }[
      status
    ] ?? 'badge-rascunho'
  );
}
</script>

<template>
  <div class="pagina-detalhe">
    <RouterLink class="voltar" to="/documentos">← Voltar para documentos</RouterLink>

    <div class="grade-painel">
      <div class="coluna-conteudo">
        <section class="cartao">
          <p v-if="carregandoDocumentos" class="estado-texto">Carregando documento...</p>
          <p v-else-if="erroDocumentos" class="erro">{{ erroDocumentos }}</p>
          <template v-else-if="documentoSelecionado">
            <div class="doc-cabecalho">
              <h1 class="doc-titulo">{{ documentoSelecionado.titulo }}</h1>
              <span class="badge" :class="classeBadge(documentoSelecionado.status)">
                {{ documentoSelecionado.status }}
              </span>
            </div>
            <hr class="separador" />
            <p class="doc-conteudo">{{ documentoSelecionado.conteudo }}</p>
          </template>
          <p v-else class="aviso">Documento não encontrado.</p>
        </section>

        <PainelDeDiagnostico :diagnostico="diagnostico" />
      </div>

      <aside class="coluna-acoes">
        <section class="cartao">
          <h2 class="titulo-acao">Editar documento</h2>
          <FormularioDocumento
            v-if="documentoSelecionado"
            :documento="documentoSelecionado"
            rotulo-botao="Salvar edição"
            :desabilitado="!podeEditarDocumento"
            @enviar="editarAtualizado"
          />
          <p v-if="!documentoSelecionado && !carregandoDocumentos" class="estado-texto">
            Carregue um documento para editar.
          </p>
          <p v-if="!podeEditarDocumento" class="aviso aviso-sm">
            {{ mensagemPermissaoAusente('documentos:editar') }}
          </p>
        </section>

        <section class="cartao">
          <h2 class="titulo-acao">Aprovação</h2>
          <p class="acao-descricao">
            Altera o status do documento para Aprovado. Requer a permissão
            <code>documentos:aprovar</code>.
          </p>
          <BotaoComPermissao
            :permitido="podeAprovarDocumento"
            :mensagem-bloqueio="mensagemPermissaoAusente('documentos:aprovar')"
            @acionar="aprovarAtual"
          >
            Aprovar documento
          </BotaoComPermissao>
        </section>
      </aside>
    </div>
  </div>
</template>

<style scoped>
.pagina-detalhe {
  display: grid;
  gap: 16px;
}

.voltar {
  color: var(--azul);
  font-size: 0.875rem;
  font-weight: 600;
  text-decoration: none;
  width: fit-content;
}

.voltar:hover {
  text-decoration: underline;
}

.coluna-conteudo {
  align-content: start;
  display: grid;
  gap: 16px;
}

.coluna-acoes {
  align-content: start;
  display: grid;
  gap: 16px;
}

.doc-cabecalho {
  align-items: flex-start;
  display: flex;
  gap: 12px;
  justify-content: space-between;
  margin-bottom: 4px;
}

.doc-titulo {
  font-size: 1.3rem;
  font-weight: 700;
  line-height: 1.3;
  margin: 0;
  min-width: 0;
}

.separador {
  border: none;
  border-top: 1px solid var(--borda);
  margin: 14px 0;
}

.doc-conteudo {
  color: #334155;
  font-size: 0.95rem;
  line-height: 1.75;
  margin: 0;
}

.titulo-acao {
  font-size: 0.9rem;
  font-weight: 700;
  margin: 0 0 14px;
}

.acao-descricao {
  color: var(--texto-suave);
  font-size: 0.875rem;
  margin: 0 0 14px;
}

.acao-descricao code {
  background: var(--cinza-fundo);
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.82rem;
  padding: 1px 5px;
}

.estado-texto {
  color: var(--texto-suave);
  font-size: 0.875rem;
  margin: 0;
}

.aviso-sm {
  font-size: 0.82rem;
  margin-top: 10px;
  padding: 8px 12px;
}
</style>
