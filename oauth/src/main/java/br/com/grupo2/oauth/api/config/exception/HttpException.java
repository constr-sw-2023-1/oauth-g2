package br.com.grupo2.oauth.api.config.exception;

import br.com.grupo2.oauth.api.config.exception.Grupo2Excepcion;
import org.springframework.http.HttpStatus;

public class HttpException extends Grupo2Excepcion {

    public HttpException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
