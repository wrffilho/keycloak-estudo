import { API_URL } from './configuracao';
import { obterToken } from './servicoDeAutenticacao';

type OpcoesRequisicao = {
  metodo?: 'GET' | 'POST' | 'PUT';
  corpo?: unknown;
  token?: string;
};

export class ErroHttp extends Error {
  constructor(
    public readonly status: number,
    mensagem: string
  ) {
    super(mensagem);
  }
}

export async function requisitar<T>(caminho: string, opcoes: OpcoesRequisicao = {}): Promise<T> {
  const token = opcoes.token ?? (await obterToken());
  const resposta = await fetch(`${API_URL}${caminho}`, {
    method: opcoes.metodo ?? 'GET',
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(opcoes.corpo ? { 'Content-Type': 'application/json' } : {})
    },
    body: opcoes.corpo ? JSON.stringify(opcoes.corpo) : undefined
  });

  if (!resposta.ok) {
    const mensagem = await lerMensagemDeErro(resposta);
    throw new ErroHttp(resposta.status, mensagem);
  }

  if (resposta.status === 204) {
    return undefined as T;
  }

  return resposta.json() as Promise<T>;
}

async function lerMensagemDeErro(resposta: Response) {
  try {
    const corpo = (await resposta.json()) as { erro?: string; message?: string };
    return corpo.erro ?? corpo.message ?? `Requisicao retornou status ${resposta.status}.`;
  } catch {
    return `Requisicao retornou status ${resposta.status}.`;
  }
}
