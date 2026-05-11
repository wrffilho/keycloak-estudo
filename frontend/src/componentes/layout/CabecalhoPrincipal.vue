<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { RouterLink } from 'vue-router';
import { useMenuDinamico } from '../../composables/useMenuDinamico';
import { useSessao } from '../../composables/useSessao';

const { autenticado, inicializarSessao } = useSessao();
const { itensDeMenu, carregandoMenu, erroMenu, carregarMenu, limparMenu } = useMenuDinamico();

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
  </header>
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
