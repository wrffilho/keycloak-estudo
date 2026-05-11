<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  autenticado: boolean;
  usuario?: string;
  carregando: boolean;
}>();

defineEmits<{
  entrar: [];
  sair: [];
}>();

const iniciais = computed(() => {
  if (!props.usuario) return '?';
  return props.usuario.slice(0, 2).toUpperCase();
});
</script>

<template>
  <section class="cartao painel-sessao">
    <h2 class="titulo-painel">Sessão</h2>

    <div v-if="carregando" class="estado-texto">
      <span class="indicador-pulsando" aria-hidden="true"></span>
      Verificando com o Keycloak...
    </div>

    <template v-else-if="autenticado">
      <div class="usuario-info">
        <div class="avatar" aria-hidden="true">{{ iniciais }}</div>
        <div class="usuario-dados">
          <span class="usuario-rotulo">Autenticado como</span>
          <strong class="usuario-nome">{{ usuario ?? 'usuário' }}</strong>
        </div>
      </div>
      <div class="tag-ativa">
        <span class="ponto-verde" aria-hidden="true"></span>
        Sessão ativa
      </div>
      <button class="botao secundario botao-bloco" type="button" @click="$emit('sair')">
        Encerrar sessão
      </button>
    </template>

    <template v-else>
      <p class="texto-nao-autenticado">
        Entre com um usuário do laboratório para testar as permissões reais.
      </p>
      <button class="botao botao-bloco" type="button" @click="$emit('entrar')">
        Entrar com Keycloak
      </button>
    </template>
  </section>
</template>

<style scoped>
.painel-sessao {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.titulo-painel {
  color: var(--texto-suave);
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.07em;
  margin: 0;
  text-transform: uppercase;
}

.estado-texto {
  align-items: center;
  color: var(--texto-suave);
  display: flex;
  font-size: 0.875rem;
  gap: 8px;
}

.indicador-pulsando {
  animation: pulsar 1.2s ease-in-out infinite;
  background: var(--azul);
  border-radius: 50%;
  display: inline-block;
  height: 8px;
  width: 8px;
}

@keyframes pulsar {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.usuario-info {
  align-items: center;
  display: flex;
  gap: 12px;
}

.avatar {
  align-items: center;
  background: var(--azul);
  border-radius: 50%;
  color: #fff;
  display: flex;
  flex-shrink: 0;
  font-size: 1rem;
  font-weight: 700;
  height: 44px;
  justify-content: center;
  width: 44px;
}

.usuario-dados {
  display: flex;
  flex-direction: column;
  gap: 1px;
  min-width: 0;
}

.usuario-rotulo {
  color: var(--texto-suave);
  font-size: 0.75rem;
}

.usuario-nome {
  font-size: 1.05rem;
  font-weight: 700;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tag-ativa {
  align-items: center;
  background: var(--verde-fundo);
  border: 1px solid var(--verde-borda);
  border-radius: 999px;
  color: var(--verde);
  display: inline-flex;
  font-size: 0.75rem;
  font-weight: 700;
  gap: 6px;
  padding: 4px 12px;
  width: fit-content;
}

.ponto-verde {
  background: #16a34a;
  border-radius: 50%;
  display: inline-block;
  height: 7px;
  width: 7px;
}

.texto-nao-autenticado {
  color: var(--texto-suave);
  font-size: 0.875rem;
  margin: 0;
}

.botao-bloco {
  width: 100%;
}
</style>
