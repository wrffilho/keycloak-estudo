<script setup lang="ts">
import { useRouter } from 'vue-router';
import CabecalhoPrincipal from './componentes/layout/CabecalhoPrincipal.vue';
import { useAlertaDeAcessoNegado } from './composables/useAlertaDeAcessoNegado';

const router = useRouter();
const { alertaAcessoNegadoVisivel, mensagemAcessoNegado, fecharAcessoNegado } =
  useAlertaDeAcessoNegado();

async function confirmarAcessoNegado() {
  fecharAcessoNegado();
  await router.push('/laboratorio');
}
</script>

<template>
  <CabecalhoPrincipal />

  <Transition name="alerta">
    <div v-if="alertaAcessoNegadoVisivel" class="alerta-acesso-negado" role="alert">
      <div class="alerta-corpo">
        <strong class="alerta-titulo">Acesso negado</strong>
        <span class="alerta-mensagem">{{ mensagemAcessoNegado }}</span>
      </div>
      <button type="button" class="botao perigo botao-sm" @click="confirmarAcessoNegado">
        OK
      </button>
    </div>
  </Transition>

  <main class="conteudo-principal">
    <RouterView v-slot="{ Component }">
      <Transition name="pagina" mode="out-in">
        <component :is="Component" />
      </Transition>
    </RouterView>
  </main>
</template>

<style scoped>
.alerta-acesso-negado {
  align-items: center;
  background: var(--vermelho-fundo);
  border: 1px solid var(--vermelho-borda);
  border-radius: 10px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  color: var(--vermelho);
  display: flex;
  gap: 16px;
  justify-content: space-between;
  left: 50%;
  max-width: min(560px, calc(100vw - 32px));
  padding: 14px 16px;
  position: fixed;
  top: 74px;
  transform: translateX(-50%);
  width: max-content;
  z-index: 30;
}

.alerta-corpo {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.alerta-titulo {
  font-size: 0.9rem;
}

.alerta-mensagem {
  font-size: 0.85rem;
  opacity: 0.85;
}

.botao-sm {
  flex-shrink: 0;
  font-size: 0.85rem;
  min-height: 34px;
  padding: 6px 14px;
}

.alerta-enter-active,
.alerta-leave-active {
  transition:
    opacity 0.2s,
    transform 0.2s;
}

.alerta-enter-from,
.alerta-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(-8px);
}

@media (max-width: 640px) {
  .alerta-acesso-negado {
    align-items: stretch;
    flex-direction: column;
    width: calc(100vw - 32px);
  }
}

.pagina-enter-active,
.pagina-leave-active {
  transition:
    opacity 0.18s ease,
    transform 0.18s ease;
}

.pagina-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.pagina-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
