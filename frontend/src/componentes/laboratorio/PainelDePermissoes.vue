<script setup lang="ts">
import type { Permissao } from '../../tipos/seguranca';

defineProps<{
  permissoes: Permissao[];
  carregando: boolean;
  erro?: string;
}>();
</script>

<template>
  <section class="cartao">
    <h2>Permissoes percebidas pela API</h2>
    <p v-if="carregando">Carregando permissoes...</p>
    <p v-else-if="erro" class="erro">{{ erro }}</p>
    <p v-else-if="permissoes.length === 0" class="aviso">Nenhuma permissao carregada.</p>
    <ul v-else class="lista-permissoes">
      <li v-for="permissao in permissoes" :key="permissao">{{ permissao }}</li>
    </ul>
  </section>
</template>

<style scoped>
.lista-permissoes {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  list-style: none;
  margin: 0;
  padding: 0;
}

.lista-permissoes li {
  background: #edf4ff;
  border: 1px solid #b7d0f3;
  border-radius: 999px;
  color: #164b82;
  font-size: 0.9rem;
  font-weight: 650;
  padding: 6px 10px;
}
</style>
