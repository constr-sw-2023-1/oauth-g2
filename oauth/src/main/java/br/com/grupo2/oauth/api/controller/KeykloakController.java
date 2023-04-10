package br.com.grupo2.oauth.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keykloak")
public class KeykloakController {



    @Operation(summary = "Cria um login")
    @RequestMapping(path = "/{base-api-url}/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criaLogin(
            @PathVariable(name = "baseUrl") String url,
            @RequestBody(required = false) String usuarios

    ) {
                return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Cria um usuário")
    @RequestMapping(path = "/{base-api-url}/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criaUsuario(
            @PathVariable(name = "baseUrl") String url,
            @RequestBody(required = false) String usuarios
    ) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Recupera todos os dados cadastrados dos usuários")
    @RequestMapping(path = "/{base-api-url}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recuperaDadosUsuario(
            @PathVariable(name = "baseUrl") String url
    ) {
        return ResponseEntity.ok().build();

    }


    @Operation(summary = "Recupera um usuário")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recuperaUsuario(
            @PathVariable(name = "id") Long id
    )  {
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Atualiza um usuário")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> atualizaUsuario(
            @PathVariable(name = "id") Long id,
            @RequestBody(required = false) String usuarios
    ) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Atualiza a senha de um usuário")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<String> atualizaSenhaUsuario(
            @PathVariable(name = "id") Long id,
            @RequestBody(required = false) String usuarios
    ) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


    @Operation(summary = "Exclui um usuário")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> excluiUsuario(
            @PathVariable(name = "id") Long id
    ) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
