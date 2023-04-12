package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.DTO.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.service.AutenticacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
                //return ResponseEntity.status(HttpStatus.OK).build();
                return  ResponseEntity.ok(authService)
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Grupo2Exception ex) {
            throw new HttpException("Não encontrado!", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(description = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseToken.class))),
            @ApiResponse(responseCode = "401", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class))) })
    @PostMapping(path = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> login(@ModelAttribute RequestLogin requestLogin) {
        try {
            log.info(String.format("POST -> /login Request Body: Username: %s Password: *****",
                    requestLogin.username()));
            ResponseToken token = tokenService.retrieveTokenWithCredentials(requestLogin);
            log.info(String.format("POST -> /login RESPONSE: %s", token));

            return new ResponseEntity<ResponseToken>(token, HttpStatus.OK);
        } catch (ApiException e) {
            log.error(e);
            return new ResponseEntity<ResponseError>(e.getERROR(), e.getSTATUS());
        }
    }


        try {
            HttpResponse<?> response = authService.login(clientId, clientSecret, grantType);
            return new ResponseEntity<>(response.body(), HttpStatus.OK);

        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
