//package br.com.grupo2.oauth.api.controller.demo;
//
//import br.com.grupo2.oauth.api.config.RefreshToken;
//import br.com.grupo2.oauth.api.config.RequestToken;
//import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
//import br.com.grupo2.oauth.api.config.exception.HttpException;
//import br.com.grupo2.oauth.api.constants.EnumError;
//import br.com.grupo2.oauth.api.service.auth.AutenticacaoService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.http.HttpResponse;
//
//
//@RestController
//@RequestMapping("api/refresh-token")
//public class RefreshTokenController {
//
//    @Autowired
//    AutenticacaoService authService;
//
//    @Operation(summary = "Atualiza o token")
//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<HttpResponse<?>> realizaLogin (
//            @RequestBody(required = false) RefreshToken refreshToken,
//            @RequestHeader(name = "Content-Type") String contentType
//    ) {
//        try {
//            return ResponseEntity.ok(authService.requestTokenLogin(refreshToken, contentType));
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Não encontrado!", EnumError.NOT_FOUND_404);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//}
