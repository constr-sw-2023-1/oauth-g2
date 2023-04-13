package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.config.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.service.auth.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    private AutenticacaoService authService;

    @Operation(summary = "Cria um login")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> realizaLogin (
            @RequestBody(required = false) RequestToken requestToken
            //@RequestHeader(name = "Content-Type") String contentType

    ) {
        try {
                authService.requestTokenLogin();
                return ResponseEntity.ok().build();
        } catch (Grupo2Exception ex) {
            throw new HttpException("Não encontrado!", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
