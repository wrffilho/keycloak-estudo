import { createApp } from 'vue';
import App from './App.vue';
import { rotas } from './rotas/rotas';
import './estilos.css';

createApp(App).use(rotas).mount('#app');
