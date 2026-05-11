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
  <div class="grade-painel">
    <section class="cartao">
      <h1>Painel do Laboratorio</h1>
      <p>
        Entre com Keycloak, veja as permissoes percebidas pela API e teste documentos sem abrir o
        Postman.
      </p>
      <div class="acoes">
        <RouterLink class="botao" to="/documentos">Abrir documentos</RouterLink>
      </div>
    </section>

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

    <PainelDeDiagnostico :diagnostico="diagnostico" />
  </div>
</template>
