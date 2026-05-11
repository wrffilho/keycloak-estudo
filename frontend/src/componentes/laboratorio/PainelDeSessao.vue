<script setup lang="ts">
defineProps<{
  autenticado: boolean;
  usuario?: string;
  carregando: boolean;
}>();

defineEmits<{
  entrar: [];
  sair: [];
}>();
</script>

<template>
  <section class="cartao">
    <h2>Sessao</h2>
    <p v-if="carregando">Verificando sessao com o Keycloak...</p>
    <template v-else-if="autenticado">
      <p>
        Usuario autenticado:
        <strong>{{ usuario ?? 'usuario sem nome' }}</strong>
      </p>
      <button class="botao secundario" type="button" @click="$emit('sair')">Sair</button>
    </template>
    <template v-else>
      <p>Entre com um usuario do laboratorio para testar permissoes reais.</p>
      <button class="botao" type="button" @click="$emit('entrar')">Entrar com Keycloak</button>
    </template>
  </section>
</template>
