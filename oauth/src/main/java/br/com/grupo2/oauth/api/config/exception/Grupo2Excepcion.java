package br.com.grupo2.oauth.api.config.exception;

import org.springframework.http.HttpStatus;

public class Grupo2Excepcion extends RuntimeException {

    public Grupo2Excepcion() {
        super();
    }

    public Grupo2Excepcion(String message) {
        super(message);
    }

    public Grupo2Excepcion(String message, Throwable ex) {
        super(message, ex);
    }

    public Grupo2Excepcion(Throwable ex) {
        super(ex);
    }

    public Grupo2Excepcion(String message, HttpStatus status) {
    }

}
