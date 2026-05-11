import Keycloak from 'keycloak-js';
import {
  KEYCLOAK_CLIENT_ID,
  KEYCLOAK_REALM,
  KEYCLOAK_RESOURCE_SERVER_CLIENT_ID,
  KEYCLOAK_URL
} from './configuracao';

const keycloak = new Keycloak({
  url: KEYCLOAK_URL,
  realm: KEYCLOAK_REALM,
  clientId: KEYCLOAK_CLIENT_ID
});

let promessaDeInit: Promise<boolean> | null = null;

export async function inicializarAutenticacao() {
  if (promessaDeInit !== null) {
    return promessaDeInit;
  }

  promessaDeInit = keycloak.init({
    onLoad: 'check-sso',
    pkceMethod: 'S256',
    checkLoginIframe: false
  });

  return promessaDeInit;
}

export function entrar() {
  return keycloak.login();
}

export function sair() {
  return keycloak.logout({
    redirectUri: window.location.origin
  });
}

export function estaAutenticado() {
  return keycloak.authenticated ?? false;
}

export function obterUsuario() {
  return keycloak.tokenParsed?.preferred_username as string | undefined;
}

export async function obterToken() {
  if (!keycloak.authenticated) {
    return undefined;
  }

  await keycloak.updateToken(30);
  return keycloak.token;
}

export async function obterRpt(permissao: string) {
  const token = await obterToken();
  if (!token) {
    return undefined;
  }

  const parametros = new URLSearchParams({
    grant_type: 'urn:ietf:params:oauth:grant-type:uma-ticket',
    audience: KEYCLOAK_RESOURCE_SERVER_CLIENT_ID,
    permission: permissao,
    client_id: KEYCLOAK_CLIENT_ID
  });

  const resposta = await fetch(
    `${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/token`,
    {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: parametros
    }
  );

  if (!resposta.ok) {
    return undefined;
  }

  const corpo = (await resposta.json()) as { access_token?: string };
  return corpo.access_token;
}
