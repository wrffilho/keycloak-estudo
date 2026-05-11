<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { RouterLink } from 'vue-router';
import FormularioDocumento from '../componentes/documentos/FormularioDocumento.vue';
import PainelDeDiagnostico from '../componentes/laboratorio/PainelDeDiagnostico.vue';
import { useDiagnosticoDeAcao } from '../composables/useDiagnosticoDeAcao';
import { useDocumentos } from '../composables/useDocumentos';
import { usePermissoes } from '../composables/usePermissoes';

const { documentos, carregandoDocumentos, erroDocumentos, carregarDocumentos, criar } =
  useDocumentos();
const { podeCriarDocumento, mensagemPermissaoAusente, carregarPermissoes } = usePermissoes();
const { diagnostico, registrarBloqueio } = useDiagnosticoDeAcao();

const novoDocumento = reactive({ titulo: '', conteudo: '' });

onMounted(async () => {
  await carregarPermissoes();
  await carregarDocumentos();
});

async function criarNovoDocumento() {
  if (!podeCriarDocumento.value) {
    registrarBloqueio('Criar documento', 'POST', '/documentos', 'documentos:criar');
    return;
  }
  await criar(novoDocumento);
  novoDocumento.titulo = '';
  novoDocumento.conteudo = '';
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
  <div class="grade-painel">
    <div class="coluna-lista">
      <section class="cartao">
        <div class="cabecalho-secao">
          <div>
            <h1 class="titulo-pagina">Documentos</h1>
            <p class="subtitulo-pagina">Teste as permissões operando documentos reais.</p>
          </div>
        </div>

        <p v-if="carregandoDocumentos" class="estado-texto">Carregando documentos...</p>
        <p v-else-if="erroDocumentos" class="erro">{{ erroDocumentos }}</p>
        <p v-else-if="documentos.length === 0" class="aviso">Nenhum documento encontrado.</p>

        <div v-else class="lista-docs">
          <article
            v-for="documento in documentos"
            :key="documento.id"
            class="cartao-documento"
          >
            <div class="doc-cabecalho">
              <h2 class="doc-titulo">{{ documento.titulo }}</h2>
              <span class="badge" :class="classeBadge(documento.status)">
                {{ documento.status }}
              </span>
            </div>
            <p class="doc-preview">{{ documento.conteudo }}</p>
            <RouterLink class="botao secundario botao-sm" :to="`/documentos/${documento.id}`">
              Ver detalhe →
            </RouterLink>
          </article>
        </div>
      </section>
    </div>

    <aside class="coluna-lateral">
      <section class="cartao">
        <h2 class="titulo-lateral">Criar documento</h2>
        <FormularioDocumento
          :documento="novoDocumento"
          rotulo-botao="Criar"
          :desabilitado="!podeCriarDocumento"
          @enviar="criarNovoDocumento"
        />
        <p v-if="!podeCriarDocumento" class="aviso aviso-sm">
          {{ mensagemPermissaoAusente('documentos:criar') }}
        </p>
      </section>

      <PainelDeDiagnostico :diagnostico="diagnostico" />
    </aside>
  </div>
</template>

<style scoped>
.coluna-lista {}

.coluna-lateral {
  align-content: start;
  display: grid;
  gap: 16px;
}

.cabecalho-secao {
  margin-bottom: 20px;
}

.titulo-pagina {
  font-size: 1.25rem;
  font-weight: 700;
  margin: 0 0 4px;
}

.subtitulo-pagina {
  color: var(--texto-suave);
  font-size: 0.875rem;
  margin: 0;
}

.titulo-lateral {
  font-size: 0.95rem;
  font-weight: 700;
  margin: 0 0 16px;
}

.estado-texto {
  color: var(--texto-suave);
  font-size: 0.875rem;
}

.lista-docs {
  display: grid;
  gap: 12px;
}

.cartao-documento {
  border: 1px solid var(--borda);
  border-radius: 10px;
  display: grid;
  gap: 10px;
  padding: 16px;
  transition:
    border-color 0.15s,
    box-shadow 0.15s;
}

.cartao-documento:hover {
  border-color: #bfdbfe;
  box-shadow: 0 4px 16px rgba(37, 99, 235, 0.08);
}

.doc-cabecalho {
  align-items: flex-start;
  display: flex;
  gap: 10px;
  justify-content: space-between;
}

.doc-titulo {
  font-size: 0.95rem;
  font-weight: 700;
  margin: 0;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.doc-preview {
  -webkit-box-orient: vertical;
  color: var(--texto-suave);
  display: -webkit-box;
  font-size: 0.875rem;
  -webkit-line-clamp: 2;
  margin: 0;
  overflow: hidden;
}

.botao-sm {
  font-size: 0.85rem;
  min-height: 34px;
  padding: 6px 14px;
  width: fit-content;
}

.aviso-sm {
  font-size: 0.82rem;
  margin-top: 8px;
  padding: 8px 12px;
}
</style>
