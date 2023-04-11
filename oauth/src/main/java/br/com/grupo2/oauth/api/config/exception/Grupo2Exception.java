package br.com.grupo2.oauth.api.config.exception;

import org.springframework.http.HttpStatus;

public class Grupo2Exception extends HttpException {

    public Grupo2Exception() {
        super();
    }

    public Grupo2Exception(String message) {
        super(message);
    }

    public Grupo2Exception(String message, Throwable ex) {
        super(message, ex);
    }

    public Grupo2Exception(Throwable ex) {
        super(ex);
    }

    public Grupo2Exception(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
