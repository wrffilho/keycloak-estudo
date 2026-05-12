import { createApp } from 'vue';
import App from './App.vue';
import { useSessao } from './composables/useSessao';
import { rotas } from './rotas/rotas';
import './estilos.css';

async function iniciarAplicacao() {
  const app = createApp(App);

  const { inicializarSessao } = useSessao();
  await inicializarSessao();

  app.use(rotas);
  app.mount('#app');
}

void iniciarAplicacao();
