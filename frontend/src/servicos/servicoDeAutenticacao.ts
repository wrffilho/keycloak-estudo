import Keycloak from 'keycloak-js';
import { KEYCLOAK_CLIENT_ID, KEYCLOAK_REALM, KEYCLOAK_URL } from './configuracao';

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
    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
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
