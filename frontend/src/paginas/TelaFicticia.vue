<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useAlertaDeAcessoNegado } from '../composables/useAlertaDeAcessoNegado';
import { useMenuDinamico } from '../composables/useMenuDinamico';
import { useSessao } from '../composables/useSessao';

const route = useRoute();
const { usuario } = useSessao();
const { garantirMenuCarregado, buscarItemPorRota } = useMenuDinamico();
const { exibirAcessoNegado } = useAlertaDeAcessoNegado();

const rotaAtual = computed(() => route.path);
const item = computed(() => buscarItemPorRota(rotaAtual.value));

onMounted(async () => {
  await garantirMenuCarregado();
  if (!item.value) {
    exibirAcessoNegado('Acesso negado. Seu usuário não tem acesso a esta tela.');
  }
});
</script>

<template>
  <section v-if="item" class="cartao tela-ficticia">
    <h1 class="tela-titulo">{{ item.rotulo }}</h1>
    <p class="sucesso">
      Usuário <strong>{{ usuario ?? 'desconhecido' }}</strong> acessou
      <strong>{{ item.rotulo }}</strong> porque o menu foi liberado pelo Keycloak.
    </p>
  </section>

  <section v-else class="cartao">
    <h1 class="tela-titulo">Validando acesso...</h1>
    <p class="aviso">Verificando se o menu foi liberado para o usuário logado.</p>
  </section>
</template>

<style scoped>
.tela-ficticia {}
.tela-titulo {
  font-size: 1.25rem;
  margin: 0 0 14px;
}
</style>
