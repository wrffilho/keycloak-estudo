export type StatusDocumento = 'RASCUNHO' | 'PUBLICADO' | 'APROVADO';

export type Documento = {
  id: string;
  titulo: string;
  conteudo: string;
  status: StatusDocumento;
};

export type CriarDocumento = {
  titulo: string;
  conteudo: string;
};

export type EditarDocumento = {
  titulo: string;
  conteudo: string;
};
