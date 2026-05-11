export type TipoItemDeMenu = 'menu' | 'submenu';

export type ItemDeMenu = {
  id: string;
  rotulo: string;
  rota: string;
  tipo: TipoItemDeMenu;
  ordem: number;
  icone?: string;
  roleNecessaria?: string;
  filhos: ItemDeMenu[];
};

export type RespostaMenu = {
  itens: ItemDeMenu[];
};
