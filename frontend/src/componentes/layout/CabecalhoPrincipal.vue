<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { RouterLink } from 'vue-router';
import { useMenuDinamico } from '../../composables/useMenuDinamico';
import { useSessao } from '../../composables/useSessao';

const { autenticado, usuario, inicializarSessao, sair } = useSessao();
const { itensDeMenu, carregandoMenu, erroMenu, carregarMenu, limparMenu } = useMenuDinamico();

const confirmarSaidaVisivel = ref(false);

function pedirConfirmacaoSaida() {
  confirmarSaidaVisivel.value = true;
}

function cancelarSaida() {
  confirmarSaidaVisivel.value = false;
}

async function confirmarSaida() {
  confirmarSaidaVisivel.value = false;
  await sair();
}

onMounted(async () => {
  await inicializarSessao();
  if (autenticado.value) {
    await carregarMenu();
  }
});

watch(autenticado, async (valor) => {
  if (valor) {
    await carregarMenu();
  } else {
    limparMenu();
  }
});
</script>

<template>
  <header class="cabecalho">
    <RouterLink class="marca" to="/laboratorio">
      <span class="marca-icone" aria-hidden="true">⚗</span>
      Laboratório Keycloak
    </RouterLink>

    <nav class="navegacao" aria-label="Menu principal">
      <span v-if="carregandoMenu" class="estado-menu">carregando...</span>
      <span v-else-if="erroMenu" class="estado-menu estado-erro" :title="erroMenu">
        erro no menu
      </span>

      <template v-else-if="itensDeMenu.length === 0">
        <RouterLink to="/laboratorio" class="nav-link">Painel</RouterLink>
        <RouterLink to="/documentos" class="nav-link">Documentos</RouterLink>
      </template>

      <template v-else>
        <div v-for="item in itensDeMenu" :key="item.id" class="grupo-menu">
          <RouterLink :to="item.rota" class="nav-link">{{ item.rotulo }}</RouterLink>
          <div v-if="item.filhos.length > 0" class="submenu">
            <RouterLink
              v-for="filho in item.filhos"
              :key="filho.id"
              :to="filho.rota"
              class="submenu-link"
            >
              {{ filho.rotulo }}
            </RouterLink>
          </div>
        </div>
      </template>
    </nav>

    <button
      v-if="autenticado"
      type="button"
      class="botao-sair"
      @click="pedirConfirmacaoSaida"
    >
      <span class="botao-sair-nome">{{ usuario }}</span>
      <span class="botao-sair-icone" aria-hidden="true">⏻</span>
    </button>
  </header>

  <Transition name="dialogo">
    <div v-if="confirmarSaidaVisivel" class="dialogo-overlay" @click.self="cancelarSaida">
      <div class="dialogo" role="dialog" aria-modal="true" aria-labelledby="dialogo-titulo">
        <p id="dialogo-titulo" class="dialogo-titulo">Até logo, {{ usuario }}!</p>
        <p class="dialogo-texto">Tem certeza que deseja encerrar a sessão?</p>
        <div class="dialogo-acoes">
          <button type="button" class="botao secundario" @click="cancelarSaida">Cancelar</button>
          <button type="button" class="botao perigo" @click="confirmarSaida">Sair</button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.cabecalho {
  align-items: center;
  background: #0f172a;
  display: flex;
  gap: 24px;
  justify-content: space-between;
  min-height: 58px;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 20;
}

.marca {
  align-items: center;
  color: #fff;
  display: flex;
  font-size: 0.95rem;
  font-weight: 700;
  gap: 8px;
  letter-spacing: 0.01em;
  text-decoration: none;
  white-space: nowrap;
}

.marca-icone {
  font-size: 1.15rem;
}

.navegacao {
  align-items: center;
  display: flex;
  gap: 2px;
}

.nav-link {
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.875rem;
  font-weight: 500;
  padding: 6px 12px;
  text-decoration: none;
  transition:
    color 0.12s,
    background 0.12s;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}

.nav-link.router-link-active {
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
}

.grupo-menu {
  position: relative;
}

.submenu {
  background: #1e293b;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  display: none;
  left: 0;
  min-width: 180px;
  padding: 6px;
  position: absolute;
  top: calc(100% + 4px);
  z-index: 10;
}

.submenu-link {
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.75);
  display: block;
  font-size: 0.85rem;
  padding: 8px 12px;
  text-decoration: none;
  transition:
    background 0.12s,
    color 0.12s;
}

.submenu-link:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}

.grupo-menu:hover .submenu,
.grupo-menu:focus-within .submenu {
  display: block;
}

.estado-menu {
  color: rgba(255, 255, 255, 0.45);
  font-size: 0.8rem;
}

.estado-erro {
  color: #fca5a5;
}

.botao-sair {
  align-items: center;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.65);
  cursor: pointer;
  display: flex;
  flex-shrink: 0;
  font-size: 0.8rem;
  gap: 6px;
  padding: 5px 10px;
  transition:
    background 0.15s,
    color 0.15s;
  white-space: nowrap;
}

.botao-sair:hover {
  background: rgba(239, 68, 68, 0.15);
  border-color: rgba(239, 68, 68, 0.35);
  color: #fca5a5;
}

.botao-sair-nome {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.botao-sair-icone {
  font-size: 0.95rem;
  line-height: 1;
}

.dialogo-overlay {
  align-items: center;
  background: rgba(0, 0, 0, 0.55);
  backdrop-filter: blur(3px);
  display: flex;
  inset: 0;
  justify-content: center;
  position: fixed;
  z-index: 50;
}

.dialogo {
  background: var(--cartao, #1e293b);
  border: 1px solid var(--borda, rgba(255, 255, 255, 0.1));
  border-radius: var(--raio, 12px);
  box-shadow: 0 24px 64px rgba(0, 0, 0, 0.4);
  max-width: 340px;
  padding: 28px 28px 24px;
  width: calc(100vw - 48px);
}

.dialogo-titulo {
  color: var(--texto, #f1f5f9);
  font-size: 1.1rem;
  font-weight: 700;
  margin: 0 0 8px;
}

.dialogo-texto {
  color: var(--texto-suave, rgba(241, 245, 249, 0.6));
  font-size: 0.9rem;
  margin: 0 0 24px;
}

.dialogo-acoes {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.dialogo-enter-active,
.dialogo-leave-active {
  transition: opacity 0.18s ease;
}

.dialogo-enter-active .dialogo,
.dialogo-leave-active .dialogo {
  transition: transform 0.18s ease;
}

.dialogo-enter-from,
.dialogo-leave-to {
  opacity: 0;
}

.dialogo-enter-from .dialogo {
  transform: scale(0.95) translateY(8px);
}

.dialogo-leave-to .dialogo {
  transform: scale(0.95) translateY(8px);
}

@media (max-width: 640px) {
  .cabecalho {
    flex-wrap: wrap;
    gap: 8px;
    padding: 12px 16px;
  }

  .submenu {
    margin-top: 4px;
    position: static;
  }
}
</style>
