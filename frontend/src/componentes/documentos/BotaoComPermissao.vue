<script setup lang="ts">
const props = defineProps<{
  permitido: boolean;
  mensagemBloqueio: string;
  variante?: 'primario' | 'secundario' | 'perigo';
}>();

defineEmits<{ acionar: [] }>();
</script>

<template>
  <div class="acao-permissao">
    <button
      class="botao botao-bloco"
      :class="{
        secundario: props.variante === 'secundario',
        perigo: props.variante === 'perigo',
      }"
      type="button"
      :disabled="!permitido"
      @click="$emit('acionar')"
    >
      <slot />
    </button>
    <div v-if="!permitido" class="mensagem-bloqueio" role="status">
      <span aria-hidden="true">🔒</span>
      {{ mensagemBloqueio }}
    </div>
  </div>
</template>

<style scoped>
.acao-permissao {
  display: grid;
  gap: 8px;
}

.botao-bloco {
  width: 100%;
}

.mensagem-bloqueio {
  align-items: center;
  background: var(--vermelho-fundo);
  border: 1px solid var(--vermelho-borda);
  border-radius: 7px;
  color: var(--vermelho);
  display: flex;
  font-size: 0.82rem;
  gap: 6px;
  padding: 8px 12px;
}
</style>
