package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.DTO.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.service.auth.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@RequestMapping("api/login")
public class LoginController {


    @Autowired
    private AutenticacaoService authService;

    @Operation(summary = "Cria um login")
    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> realizaLogin (
            @RequestBody(required = false) RequestToken requestToken
    ) {
        try {
            if (requestToken != null) {
                authService.requestTokenLogin(requestToken);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Grupo2Exception ex) {
            throw new HttpException("Não encontrado!", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
