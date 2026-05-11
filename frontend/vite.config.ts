import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173
  },
  preview: {
    port: 5173
  },
  test: {
    environment: 'jsdom',
    globals: true,
    coverage: {
      provider: 'v8',
      reporter: ['text', 'html'],
      include: [
        'src/composables/useDiagnosticoDeAcao.ts',
        'src/composables/usePermissoes.ts',
        'src/servicos/clienteHttp.ts',
        'src/componentes/documentos/BotaoComPermissao.vue',
        'src/rotas/rotas.ts'
      ],
      thresholds: {
        lines: 80,
        functions: 80,
        branches: 80,
        statements: 80
      }
    }
  }
});
