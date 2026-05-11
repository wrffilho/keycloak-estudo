import type { RespostaMenu } from '../tipos/menu';
import { requisitar } from './clienteHttp';

export function buscarMenu() {
  return requisitar<RespostaMenu>('/usuario/menu');
}
