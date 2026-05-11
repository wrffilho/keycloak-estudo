package br.com.wanderlei.keycloakestudo.menu;

public class ErroCatalogoDeMenuException extends RuntimeException {

    public ErroCatalogoDeMenuException(String mensagem) {
        super(mensagem);
    }

    public ErroCatalogoDeMenuException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
