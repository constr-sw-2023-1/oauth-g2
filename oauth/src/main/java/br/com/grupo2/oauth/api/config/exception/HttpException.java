package br.com.grupo2.oauth.api.config.exception;

import br.com.grupo2.oauth.api.constants.EnumError;
import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable ex) {
        super(message, ex);
    }

    public HttpException(Throwable ex) {
        super(ex);
    }

    public HttpException(String message, EnumError enumError) {
    }
}
