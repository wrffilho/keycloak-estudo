import type { PerfilUsuario, RespostaPermissoes } from '../tipos/seguranca';
import { requisitar } from './clienteHttp';

export function buscarPerfil() {
  return requisitar<PerfilUsuario>('/usuario/perfil');
}

export function buscarPermissoes() {
  return requisitar<RespostaPermissoes>('/usuario/permissoes');
}
