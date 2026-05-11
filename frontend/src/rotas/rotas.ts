import { createRouter, createWebHistory } from 'vue-router';
import PainelDoLaboratorio from '../paginas/PainelDoLaboratorio.vue';
import Documentos from '../paginas/Documentos.vue';
import DocumentoDetalhe from '../paginas/DocumentoDetalhe.vue';
import TelaFicticia from '../paginas/TelaFicticia.vue';

export const rotas = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/laboratorio'
    },
    {
      path: '/laboratorio',
      name: 'laboratorio',
      component: PainelDoLaboratorio
    },
    {
      path: '/documentos',
      name: 'documentos',
      component: Documentos
    },
    {
      path: '/documentos/:id',
      name: 'documento-detalhe',
      component: DocumentoDetalhe,
      props: true
    },
    {
      path: '/tela/:id',
      name: 'tela-ficticia',
      component: TelaFicticia
    }
  ]
});
