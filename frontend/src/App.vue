<script setup lang="ts">
import CabecalhoPrincipal from './componentes/layout/CabecalhoPrincipal.vue';
</script>

<template>
  <CabecalhoPrincipal />
  <main class="conteudo-principal">
    <RouterView />
  </main>
</template>
