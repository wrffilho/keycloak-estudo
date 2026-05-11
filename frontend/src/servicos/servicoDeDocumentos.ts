import type { CriarDocumento, Documento, EditarDocumento } from '../tipos/documento';
import { requisitar } from './clienteHttp';
import { obterRpt } from './servicoDeAutenticacao';

async function requisitarDocumento<T>(
  caminho: string,
  escopo: 'ler' | 'criar' | 'editar' | 'aprovar',
  opcoes: Parameters<typeof requisitar<T>>[1] = {}
) {
  const rpt = await obterRpt(`documentos#${escopo}`);
  return requisitar<T>(caminho, {
    ...opcoes,
    token: rpt
  });
}

export function listarDocumentos() {
  return requisitarDocumento<Documento[]>('/documentos', 'ler');
}

export function buscarDocumentoPorId(id: string) {
  return requisitarDocumento<Documento>(`/documentos/${id}`, 'ler');
}

export function criarDocumento(documento: CriarDocumento) {
  return requisitarDocumento<Documento>('/documentos', 'criar', {
    metodo: 'POST',
    corpo: documento
  });
}

export function editarDocumento(id: string, documento: EditarDocumento) {
  return requisitarDocumento<Documento>(`/documentos/${id}`, 'editar', {
    metodo: 'PUT',
    corpo: documento
  });
}

export function aprovarDocumento(id: string) {
  return requisitarDocumento<Documento>(`/documentos/${id}/aprovar`, 'aprovar', {
    metodo: 'POST'
  });
}
