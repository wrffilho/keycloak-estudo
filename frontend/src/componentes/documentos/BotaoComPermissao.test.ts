import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import BotaoComPermissao from './BotaoComPermissao.vue';

describe('BotaoComPermissao', () => {
  it('emite acao quando a permissao existe', async () => {
    const wrapper = mount(BotaoComPermissao, {
      props: {
        permitido: true,
        mensagemBloqueio: 'Falta a permissao documentos:criar.'
      },
      slots: {
        default: 'Criar'
      }
    });

    await wrapper.get('button').trigger('click');

    expect(wrapper.emitted('acionar')).toHaveLength(1);
  });

  it('desabilita o botao e mostra explicacao quando falta permissao', () => {
    const wrapper = mount(BotaoComPermissao, {
      props: {
        permitido: false,
        mensagemBloqueio: 'Falta a permissao documentos:aprovar.'
      },
      slots: {
        default: 'Aprovar'
      }
    });

    expect(wrapper.get('button').attributes('disabled')).toBeDefined();
    expect(wrapper.text()).toContain('documentos:aprovar');
  });
});
