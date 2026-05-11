import type { CriarDocumento, Documento, EditarDocumento } from '../tipos/documento';
import { requisitar } from './clienteHttp';

export function listarDocumentos() {
  return requisitar<Documento[]>('/documentos');
}

export function buscarDocumentoPorId(id: string) {
  return requisitar<Documento>(`/documentos/${id}`);
}

export function criarDocumento(documento: CriarDocumento) {
  return requisitar<Documento>('/documentos', {
    metodo: 'POST',
    corpo: documento
  });
}

export function editarDocumento(id: string, documento: EditarDocumento) {
  return requisitar<Documento>(`/documentos/${id}`, {
    metodo: 'PUT',
    corpo: documento
  });
}

export function aprovarDocumento(id: string) {
  return requisitar<Documento>(`/documentos/${id}/aprovar`, {
    metodo: 'POST'
  });
}
