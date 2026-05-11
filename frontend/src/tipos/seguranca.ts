export type Permissao =
  | 'documentos:ler'
  | 'documentos:criar'
  | 'documentos:editar'
  | 'documentos:aprovar';

export type PerfilUsuario = {
  usuario: string;
  autenticado: boolean;
};

export type RespostaPermissoes = {
  permissoes: Permissao[];
};

export type SessaoUsuario = {
  autenticado: boolean;
  usuario?: string;
};
