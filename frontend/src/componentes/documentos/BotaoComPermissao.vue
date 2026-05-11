<script setup lang="ts">
const props = defineProps<{
  permitido: boolean;
  mensagemBloqueio: string;
  variante?: 'primario' | 'secundario' | 'perigo';
}>();

defineEmits<{
  acionar: [];
}>();
</script>

<template>
  <div class="acao-com-permissao">
    <button
      class="botao"
      :class="{ secundario: props.variante === 'secundario', perigo: props.variante === 'perigo' }"
      type="button"
      :disabled="!permitido"
      @click="$emit('acionar')"
    >
      <slot />
    </button>
    <small v-if="!permitido">{{ mensagemBloqueio }}</small>
  </div>
</template>

<style scoped>
.acao-com-permissao {
  display: grid;
  gap: 4px;
}

small {
  color: #7d2525;
  max-width: 240px;
}
</style>
