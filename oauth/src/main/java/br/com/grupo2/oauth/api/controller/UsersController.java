package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.DTO.AtualizaSenhaUsuarioDTO;
import br.com.grupo2.oauth.api.DTO.AtualizaUsuarioDTO;
import br.com.grupo2.oauth.api.DTO.CriacaoUsuarioDTO;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsersController {


    @Operation(summary = "Cria um usuário")
    @RequestMapping(path = "/{base-api-url}/cria-usuario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criaUsuario (
            @PathVariable(name = "url") String url,
            @RequestBody(required = false) String usuarios
    ) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Lista todos os usuários")
    @RequestMapping(path = "/lista-usuarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> recuperaDadosUsuario (
    ) {
        try {
            //return
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Grupo2Exception e) {
            throw new HttpException("Não foi possível fazer a listagem", HttpStatus.NOT_FOUND);
        }


    }


//    @Operation(summary = "Cria um login")
//    @RequestMapping(path = "/{base-api-url}/cria-login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> criaLogin (
//            @PathVariable(name = "url") String url,
//            @RequestBody(required = false) CriacaoUsuarioDTO usuarioDTO
//    ) {
//        try {
//            if (usuarioDTO != null) {
//                return ResponseEntity.status(HttpStatus.CREATED).build();
//            }
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Não encontrado!", HttpStatus.NOT_FOUND);
//        }
//    }


//    @Operation(summary = "Recupera um usuário")
//    @RequestMapping(path = "/usuarios/recupera/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> recuperaUsuario (
//            @PathVariable(name = "id") Long id
//    )  {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).build();
//
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Erro ao recuperar dados do usuário", HttpStatus.NOT_FOUND);
//        }
//    }


//    @Operation(summary = "Atualiza um usuário")
//    @RequestMapping(path = "/usuarios/atualiza/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> atualizaUsuario (
//            @PathVariable(name = "id") Long id,
//            @RequestBody(required = false) AtualizaUsuarioDTO atualizaUsuarioDTO
//    ) {
//        try {
//            if (atualizaUsuarioDTO != null) {
//                return ResponseEntity.status(HttpStatus.CREATED).build();
//            }
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Erro ao atualizar usuário", HttpStatus.NOT_FOUND);
//        }
//    }


//    @Operation(summary = "Atualiza a senha de um usuário")
//    @RequestMapping(path = "/usuarios/corrige/{id}", method = RequestMethod.PATCH)
//    public ResponseEntity<?> atualizaSenhaUsuario (
//            @PathVariable(name = "id") Long id,
//            @RequestBody(required = false) AtualizaSenhaUsuarioDTO atualizaSenhaUsuarioDTO
//    ) {
//        try {
//            if (atualizaSenhaUsuarioDTO != null) {
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Erro ao atualizar a senha!", HttpStatus.NOT_FOUND);
//        }
//    }


//    @Operation(summary = "Exclui um usuário")
//    @RequestMapping(path = "/usuarios/deleta/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<?> excluiUsuario (
//            @PathVariable(name = "id") Long id
//    ) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).build();
//
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Erro ao Deletar!", HttpStatus.NOT_FOUND);
//        }
//    }
}
