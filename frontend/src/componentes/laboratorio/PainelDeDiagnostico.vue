<script setup lang="ts">
import { computed } from 'vue';
import type { DiagnosticoDeAcao } from '../../tipos/diagnostico';

const props = defineProps<{
  diagnostico: DiagnosticoDeAcao;
}>();

const classeStatus = computed(() => {
  const s = props.diagnostico.status;
  if (!s) return 'neutro';
  if (s < 400) return 'sucesso';
  if (s === 401) return 'alerta';
  return 'erro';
});

const classeMetodo = computed(
  () =>
    ({
      GET: 'metodo-get',
      POST: 'metodo-post',
      PUT: 'metodo-put',
    })[props.diagnostico.metodo] ?? '',
);
</script>

<template>
  <section class="cartao diagnostico" :class="`diagnostico-${classeStatus}`">
    <div class="diagnostico-cabecalho">
      <h2 class="titulo-painel">Diagnóstico da última ação</h2>
      <div v-if="diagnostico.status" class="status-badge" :class="`status-${classeStatus}`">
        {{ diagnostico.status }}
      </div>
    </div>

    <div class="diagnostico-corpo">
      <div class="diagnostico-linha">
        <span class="rotulo">Ação</span>
        <span class="valor">{{ diagnostico.acao }}</span>
      </div>
      <div class="diagnostico-linha">
        <span class="rotulo">Endpoint</span>
        <span class="valor endpoint-valor">
          <span class="badge-metodo" :class="classeMetodo">{{ diagnostico.metodo }}</span>
          <code class="endpoint-path">{{ diagnostico.endpoint }}</code>
        </span>
      </div>
    </div>

    <p class="diagnostico-mensagem" :class="diagnostico.permitido ? 'msg-sucesso' : 'msg-erro'">
      {{ diagnostico.mensagem }}
    </p>
  </section>
</template>

<style scoped>
.diagnostico {
  border-left: 4px solid var(--borda);
}

.diagnostico-sucesso {
  border-left-color: #16a34a;
}

.diagnostico-alerta {
  border-left-color: #d97706;
}

.diagnostico-erro {
  border-left-color: #dc2626;
}

.diagnostico-cabecalho {
  align-items: center;
  display: flex;
  gap: 12px;
  justify-content: space-between;
  margin-bottom: 16px;
}

.titulo-painel {
  color: var(--texto-suave);
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.07em;
  margin: 0;
  text-transform: uppercase;
}

.status-badge {
  border-radius: 6px;
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
  font-weight: 700;
  padding: 4px 12px;
}

.status-neutro {
  background: var(--cinza-fundo);
  color: var(--cinza);
}

.status-sucesso {
  background: var(--verde-fundo);
  color: var(--verde);
}

.status-alerta {
  background: var(--amarelo-fundo);
  color: var(--amarelo);
}

.status-erro {
  background: var(--vermelho-fundo);
  color: var(--vermelho);
}

.diagnostico-corpo {
  display: grid;
  gap: 10px;
  margin-bottom: 14px;
}

.diagnostico-linha {
  align-items: baseline;
  display: flex;
  gap: 12px;
}

.rotulo {
  color: var(--texto-suave);
  flex-shrink: 0;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  min-width: 66px;
  text-transform: uppercase;
}

.valor {
  font-size: 0.9rem;
  overflow-wrap: anywhere;
}

.endpoint-valor {
  align-items: center;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.badge-metodo {
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.68rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  padding: 2px 7px;
  white-space: nowrap;
}

.metodo-get {
  background: var(--azul-fundo);
  color: var(--azul-escuro);
}

.metodo-post {
  background: var(--verde-fundo);
  color: var(--verde);
}

.metodo-put {
  background: var(--amarelo-fundo);
  color: var(--amarelo);
}

.endpoint-path {
  background: none;
  font-family: 'Courier New', monospace;
  font-size: 0.875rem;
  overflow-wrap: anywhere;
}

.diagnostico-mensagem {
  border-radius: 7px;
  font-size: 0.875rem;
  margin: 0;
  padding: 10px 14px;
}

.msg-sucesso {
  background: var(--verde-fundo);
  border: 1px solid var(--verde-borda);
  color: var(--verde);
}

.msg-erro {
  background: var(--vermelho-fundo);
  border: 1px solid var(--vermelho-borda);
  color: var(--vermelho);
}
</style>
