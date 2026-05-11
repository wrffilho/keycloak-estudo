<script setup lang="ts">
import { onMounted, watch } from 'vue';
import { RouterLink } from 'vue-router';
import PainelDeDiagnostico from '../componentes/laboratorio/PainelDeDiagnostico.vue';
import PainelDePermissoes from '../componentes/laboratorio/PainelDePermissoes.vue';
import PainelDeSessao from '../componentes/laboratorio/PainelDeSessao.vue';
import { useDiagnosticoDeAcao } from '../composables/useDiagnosticoDeAcao';
import { usePermissoes } from '../composables/usePermissoes';
import { useSessao } from '../composables/useSessao';

const { autenticado, carregandoSessao, usuario, inicializarSessao, entrar, sair } = useSessao();
const { permissoes, carregandoPermissoes, erroPermissoes, carregarPermissoes } = usePermissoes();
const { diagnostico } = useDiagnosticoDeAcao();

onMounted(async () => {
  await inicializarSessao();
  if (autenticado.value) {
    await carregarPermissoes();
  }
});

watch(autenticado, async (valor) => {
  if (valor) {
    await carregarPermissoes();
  }
});
</script>

<template>
  <div class="pagina-painel">
    <section class="cartao hero">
      <div class="hero-texto">
        <h1 class="hero-titulo">Painel do Laboratório Keycloak</h1>
        <p class="hero-descricao">
          Entre com o Keycloak, veja as permissões reais percebidas pela API e teste documentos sem
          abrir o Postman.
        </p>
      </div>
      <div v-if="!carregandoSessao" class="hero-acao">
        <template v-if="!autenticado">
          <button class="botao botao-hero" type="button" @click="entrar">
            Entrar com Keycloak
          </button>
          <p class="hero-dica">Use leitor, editor ou aprovador para ver permissões diferentes.</p>
        </template>
        <RouterLink v-else class="botao botao-hero" to="/documentos">
          Ir para documentos →
        </RouterLink>
      </div>
    </section>

    <div class="grade-3-colunas">
      <PainelDeSessao
        :autenticado="autenticado"
        :usuario="usuario"
        :carregando="carregandoSessao"
        @entrar="entrar"
        @sair="sair"
      />

      <PainelDePermissoes
        :permissoes="permissoes"
        :carregando="carregandoPermissoes"
        :erro="erroPermissoes"
      />

      <section class="cartao painel-documentos">
        <h2 class="painel-titulo">Documentos</h2>
        <p class="painel-descricao">
          Operação de documentos reais para exercitar permissões de leitura, criação, edição e
          aprovação.
        </p>
        <div class="permissoes-necessarias">
          <span class="badge-permissao">documentos:ler</span>
          <span class="badge-permissao">documentos:criar</span>
          <span class="badge-permissao">documentos:editar</span>
          <span class="badge-permissao">documentos:aprovar</span>
        </div>
        <RouterLink class="botao botao-bloco" to="/documentos">Abrir documentos</RouterLink>
      </section>
    </div>

    <PainelDeDiagnostico :diagnostico="diagnostico" />
  </div>
</template>

<style scoped>
.pagina-painel {
  display: grid;
  gap: 20px;
}

/* ── Hero ── */

.hero {
  align-items: center;
  background: linear-gradient(135deg, #0f2544 0%, #1d4ed8 100%);
  border-color: transparent;
  color: #fff;
  display: flex;
  gap: 32px;
  justify-content: space-between;
}

.hero-texto {
  min-width: 0;
}

.hero-titulo {
  font-size: 1.55rem;
  font-weight: 800;
  line-height: 1.2;
  margin: 0 0 10px;
}

.hero-descricao {
  color: rgba(255, 255, 255, 0.82);
  font-size: 0.95rem;
  margin: 0;
  max-width: 500px;
}

.hero-acao {
  flex-shrink: 0;
  text-align: center;
}

.botao-hero {
  background: #fff;
  border-color: #fff;
  color: #1d4ed8;
  font-size: 0.95rem;
  min-height: 46px;
  padding: 11px 24px;
  white-space: nowrap;
}

.botao-hero:hover {
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.hero-dica {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.78rem;
  margin: 8px 0 0;
}

/* ── Painel documentos ── */

.painel-documentos {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.painel-titulo {
  font-size: 0.85rem;
  font-weight: 700;
  margin: 0;
}

.painel-descricao {
  color: var(--texto-suave);
  font-size: 0.85rem;
  margin: 0;
}

.permissoes-necessarias {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.badge-permissao {
  background: var(--cinza-fundo);
  border: 1px solid var(--cinza-borda);
  border-radius: 999px;
  color: var(--cinza);
  font-family: 'Courier New', monospace;
  font-size: 0.68rem;
  font-weight: 600;
  padding: 2px 8px;
}

.botao-bloco {
  margin-top: auto;
  width: 100%;
}

/* ── Responsivo ── */

@media (max-width: 700px) {
  .hero {
    align-items: flex-start;
    flex-direction: column;
    gap: 20px;
  }

  .botao-hero {
    width: 100%;
  }

  .hero-titulo {
    font-size: 1.25rem;
  }
}
</style>
