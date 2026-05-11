<script setup lang="ts">
import type { DiagnosticoDeAcao } from '../../tipos/diagnostico';

defineProps<{
  diagnostico: DiagnosticoDeAcao;
}>();
</script>

<template>
  <section class="cartao">
    <h2>Diagnostico da ultima acao</h2>
    <dl class="diagnostico">
      <div>
        <dt>Acao</dt>
        <dd>{{ diagnostico.acao }}</dd>
      </div>
      <div>
        <dt>Endpoint</dt>
        <dd>{{ diagnostico.metodo }} {{ diagnostico.endpoint }}</dd>
      </div>
      <div>
        <dt>Status</dt>
        <dd>{{ diagnostico.status ?? '-' }}</dd>
      </div>
    </dl>
    <p :class="diagnostico.permitido ? 'sucesso' : 'erro'">{{ diagnostico.mensagem }}</p>
  </section>
</template>

<style scoped>
.diagnostico {
  display: grid;
  gap: 10px;
  margin: 0 0 12px;
}

.diagnostico div {
  display: grid;
  gap: 2px;
}

dt {
  color: #627084;
  font-size: 0.85rem;
  font-weight: 700;
}

dd {
  margin: 0;
  overflow-wrap: anywhere;
}
</style>
