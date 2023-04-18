package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.config.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.constants.EnumError;
import br.com.grupo2.oauth.api.service.auth.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    AutenticacaoService authService;

    @Operation(summary = "Cria um login")
    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<HttpResponse<?>> realizaLogin (
            @RequestBody(required = false) RequestToken requestToken,
            @RequestHeader(name = "Content-Type") String contentType
    ) {
        try {
            return ResponseEntity.ok(authService.requestTokenLogin(requestToken, contentType));
        } catch (Grupo2Exception ex) {
            throw new HttpException("Não encontrado!", EnumError.NOT_FOUND_404);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
