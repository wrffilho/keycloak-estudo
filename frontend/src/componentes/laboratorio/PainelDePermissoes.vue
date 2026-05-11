<script setup lang="ts">
import type { Permissao } from '../../tipos/seguranca';

const props = defineProps<{
  permissoes: Permissao[];
  carregando: boolean;
  erro?: string;
}>();

const todasPermissoes: { chave: Permissao; rotulo: string }[] = [
  { chave: 'documentos:ler', rotulo: 'Ler' },
  { chave: 'documentos:criar', rotulo: 'Criar' },
  { chave: 'documentos:editar', rotulo: 'Editar' },
  { chave: 'documentos:aprovar', rotulo: 'Aprovar' },
];

function temPermissao(permissao: Permissao) {
  return props.permissoes.includes(permissao);
}
</script>

<template>
  <section class="cartao painel-permissoes">
    <h2 class="titulo-painel">Permissões (API)</h2>

    <p v-if="carregando" class="estado-texto">Carregando permissões...</p>
    <p v-else-if="erro" class="erro">{{ erro }}</p>

    <ul v-else class="lista-permissoes">
      <li
        v-for="perm in todasPermissoes"
        :key="perm.chave"
        class="item-permissao"
        :class="{ ativa: temPermissao(perm.chave) }"
      >
        <span class="icone-permissao" aria-hidden="true">
          {{ temPermissao(perm.chave) ? '✓' : '○' }}
        </span>
        <div class="permissao-texto">
          <span class="permissao-rotulo">{{ perm.rotulo }}</span>
          <code class="permissao-chave">{{ perm.chave }}</code>
        </div>
      </li>
    </ul>

    <p v-if="!carregando && !erro && !permissoes.length" class="dica-entrar">
      Entre para ver as permissões do seu usuário.
    </p>
  </section>
</template>

<style scoped>
.painel-permissoes {
  display: flex;
  flex-direction: column;
  gap: 12px;
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
  color: var(--texto-suave);
  font-size: 0.875rem;
  margin: 0;
}

.lista-permissoes {
  display: grid;
  gap: 7px;
  list-style: none;
  margin: 0;
  padding: 0;
}

.item-permissao {
  align-items: center;
  border: 1px solid var(--borda);
  border-radius: 8px;
  display: flex;
  gap: 10px;
  opacity: 0.4;
  padding: 9px 12px;
  transition:
    opacity 0.15s,
    border-color 0.15s,
    background 0.15s;
}

.item-permissao.ativa {
  background: var(--azul-fundo);
  border-color: #bfdbfe;
  opacity: 1;
}

.icone-permissao {
  color: #94a3b8;
  flex-shrink: 0;
  font-size: 0.95rem;
  font-weight: 700;
  text-align: center;
  width: 16px;
}

.item-permissao.ativa .icone-permissao {
  color: var(--azul);
}

.permissao-texto {
  display: flex;
  flex-direction: column;
  gap: 1px;
  min-width: 0;
}

.permissao-rotulo {
  font-size: 0.85rem;
  font-weight: 600;
}

.permissao-chave {
  background: none;
  color: var(--texto-suave);
  font-family: 'Courier New', monospace;
  font-size: 0.72rem;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dica-entrar {
  color: var(--texto-suave);
  font-size: 0.8rem;
  margin: 0;
}
</style>
