package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.config.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.service.auth.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;


@Log4j2
@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    private AutenticacaoService authService;

    @Operation(summary = "Cria um login")
    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<HttpResponse<?>> realizaLogin (
            @RequestBody(required = false) RequestToken requestToken,
            @RequestHeader(name = "Content-Type") String contentType
    ) {
        try {
            return ResponseEntity.ok(authService.requestTokenLogin(requestToken, contentType));
        } catch (Grupo2Exception ex) {
            throw new HttpException("Não encontrado!", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
