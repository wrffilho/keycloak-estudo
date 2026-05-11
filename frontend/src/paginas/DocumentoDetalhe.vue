<script setup lang="ts">
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';
import BotaoComPermissao from '../componentes/documentos/BotaoComPermissao.vue';
import FormularioDocumento from '../componentes/documentos/FormularioDocumento.vue';
import PainelDeDiagnostico from '../componentes/laboratorio/PainelDeDiagnostico.vue';
import { useDiagnosticoDeAcao } from '../composables/useDiagnosticoDeAcao';
import { useDocumentos } from '../composables/useDocumentos';
import { usePermissoes } from '../composables/usePermissoes';
import type { EditarDocumento } from '../tipos/documento';

const route = useRoute();
const id = String(route.params.id);

const {
  documentoSelecionado,
  carregandoDocumentos,
  erroDocumentos,
  carregarDocumento,
  editar,
  aprovar
} = useDocumentos();
const {
  podeEditarDocumento,
  podeAprovarDocumento,
  mensagemPermissaoAusente,
  carregarPermissoes
} = usePermissoes();
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
</script>

<template>
  <div class="grade-painel">
    <section class="cartao">
      <h1>Detalhe do documento</h1>
      <p v-if="carregandoDocumentos">Carregando documento...</p>
      <p v-else-if="erroDocumentos" class="erro">{{ erroDocumentos }}</p>
      <template v-else-if="documentoSelecionado">
        <h2>{{ documentoSelecionado.titulo }}</h2>
        <p>{{ documentoSelecionado.conteudo }}</p>
        <p><strong>Status:</strong> {{ documentoSelecionado.status }}</p>
      </template>
      <p v-else class="aviso">Documento nao encontrado.</p>
    </section>

    <section class="cartao">
      <h2>Editar</h2>
      <FormularioDocumento
        v-if="documentoSelecionado"
        :documento="documentoSelecionado"
        rotulo-botao="Salvar edicao"
        :desabilitado="!podeEditarDocumento"
        @enviar="editarAtualizado"
      />
      <p v-if="!podeEditarDocumento" class="aviso">
        {{ mensagemPermissaoAusente('documentos:editar') }}
      </p>
    </section>

    <section class="cartao">
      <h2>Aprovacao</h2>
      <BotaoComPermissao
        :permitido="podeAprovarDocumento"
        :mensagem-bloqueio="mensagemPermissaoAusente('documentos:aprovar')"
        @acionar="aprovarAtual"
      >
        Aprovar documento
      </BotaoComPermissao>
    </section>

    <PainelDeDiagnostico :diagnostico="diagnostico" />
  </div>
</template>
