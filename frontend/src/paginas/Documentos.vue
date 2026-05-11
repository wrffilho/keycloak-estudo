<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { RouterLink } from 'vue-router';
import FormularioDocumento from '../componentes/documentos/FormularioDocumento.vue';
import PainelDeDiagnostico from '../componentes/laboratorio/PainelDeDiagnostico.vue';
import { useDiagnosticoDeAcao } from '../composables/useDiagnosticoDeAcao';
import { useDocumentos } from '../composables/useDocumentos';
import { usePermissoes } from '../composables/usePermissoes';

const { documentos, carregandoDocumentos, erroDocumentos, carregarDocumentos, criar } = useDocumentos();
const { podeCriarDocumento, mensagemPermissaoAusente, carregarPermissoes } = usePermissoes();
const { diagnostico, registrarBloqueio } = useDiagnosticoDeAcao();

const novoDocumento = reactive({
  titulo: '',
  conteudo: ''
});

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
</script>

<template>
  <div class="grade-painel">
    <section class="cartao">
      <h1>Documentos</h1>
      <p>Use os documentos para observar quais acoes seu usuario pode executar.</p>

      <p v-if="carregandoDocumentos">Carregando documentos...</p>
      <p v-else-if="erroDocumentos" class="erro">{{ erroDocumentos }}</p>
      <ul v-else class="lista-documentos">
        <li v-for="documento in documentos" :key="documento.id" class="documento-item">
          <h2>{{ documento.titulo }}</h2>
          <p>{{ documento.conteudo }}</p>
          <p><strong>Status:</strong> {{ documento.status }}</p>
          <RouterLink class="botao secundario" :to="`/documentos/${documento.id}`">
            Ver detalhe
          </RouterLink>
        </li>
      </ul>
    </section>

    <aside class="cartao">
      <h2>Criar documento</h2>
      <FormularioDocumento
        :documento="novoDocumento"
        rotulo-botao="Criar"
        :desabilitado="!podeCriarDocumento"
        @enviar="criarNovoDocumento"
      />
      <p v-if="!podeCriarDocumento" class="aviso">
        {{ mensagemPermissaoAusente('documentos:criar') }}
      </p>
    </aside>

    <PainelDeDiagnostico :diagnostico="diagnostico" />
  </div>
</template>
