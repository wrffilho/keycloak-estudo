import { describe, expect, it } from 'vitest';
import { rotas } from './rotas';

describe('rotas', () => {
  it('declara as telas principais do laboratorio com Vue Router', () => {
    const caminhos = rotas.getRoutes().map((rota) => rota.path);

    expect(caminhos).toContain('/laboratorio');
    expect(caminhos).toContain('/documentos');
    expect(caminhos).toContain('/documentos/:id');
    expect(caminhos).toContain('/tela/:id');
  });
});
