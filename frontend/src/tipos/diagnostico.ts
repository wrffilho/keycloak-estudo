export type MetodoHttp = 'GET' | 'POST' | 'PUT';

export type DiagnosticoDeAcao = {
  acao: string;
  metodo: MetodoHttp;
  endpoint: string;
  status?: number;
  mensagem: string;
  permitido: boolean;
};
