package br.com.grupo2.oauth.api.config.exception;

import java.sql.SQLException;

public class DatabaseConnectionException extends RuntimeException {

    DatabaseConnectionException() {
        super();
    }

    DatabaseConnectionException(String message) {
        super();
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
