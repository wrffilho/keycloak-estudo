<script setup lang="ts">
import { reactive, watch } from 'vue';
import type { CriarDocumento, Documento } from '../../tipos/documento';

const props = defineProps<{
  documento?: Documento | CriarDocumento;
  rotuloBotao: string;
  desabilitado?: boolean;
}>();

const emit = defineEmits<{
  enviar: [{ titulo: string; conteudo: string }];
}>();

const formulario = reactive({
  titulo: props.documento?.titulo ?? '',
  conteudo: props.documento?.conteudo ?? ''
});

watch(
  () => props.documento,
  (documento) => {
    formulario.titulo = documento?.titulo ?? '';
    formulario.conteudo = documento?.conteudo ?? '';
  }
);

function enviarFormulario() {
  emit('enviar', {
    titulo: formulario.titulo,
    conteudo: formulario.conteudo
  });
}
</script>

<template>
  <form @submit.prevent="enviarFormulario">
    <label class="campo">
      <span>Titulo</span>
      <input v-model="formulario.titulo" :disabled="desabilitado" required />
    </label>
    <label class="campo">
      <span>Conteudo</span>
      <textarea v-model="formulario.conteudo" :disabled="desabilitado" required rows="4" />
    </label>
    <button class="botao" type="submit" :disabled="desabilitado">{{ rotuloBotao }}</button>
  </form>
</template>
