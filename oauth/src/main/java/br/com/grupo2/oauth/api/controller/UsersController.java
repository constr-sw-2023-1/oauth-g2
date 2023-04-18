package br.com.grupo2.oauth.api.controller;

import br.com.grupo2.oauth.api.DTO.AtualizaSenhaUsuarioDTO;
import br.com.grupo2.oauth.api.DTO.AtualizaUsuarioDTO;
import br.com.grupo2.oauth.api.DTO.CriacaoUsuarioDTO;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.constants.EnumError;
import br.com.grupo2.oauth.api.service.auth.users.UsersImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {

    private Keycloak keycloak;

    private UsersImpl users;

    @Value("${keycloak.realm}")
    private String realm;


    @Operation(summary = "Lista todos os usuários")
    @RequestMapping(path = "/lista-usuarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRepresentation>> findAll (
            @RequestHeader(name = "header") HttpHeaders header
    ) {
        try {
            List<UserRepresentation> userRepresentation = keycloak.realm(this.realm).users().list();
            if (userRepresentation.size() != 0) {
                return new ResponseEntity<>(userRepresentation, header, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Grupo2Exception e) {
            throw new HttpException("Não foi possível fazer a listagem", EnumError.NOT_FOUND_404);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um usuário")
    @RequestMapping(path = "/cria-usuario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> criaUsuario (
            @RequestBody(required = false) CriacaoUsuarioDTO criacaoUsuarioDTO,
            @RequestHeader(name = "header") HttpHeaders header
    ) {
        try {
            UserRepresentation userRepresentation = getUsuarioCriado(criacaoUsuarioDTO);
            Response response = keycloak.realm(realm).users().create(userRepresentation);
            return new ResponseEntity<>(response, header, HttpStatus.OK);

        } catch (Grupo2Exception e) {
            throw new HttpException("Não foi possível fazer a listagem", EnumError.NOT_FOUND_404);
        }
    }


//    @Operation(summary = "Recupera um usuário")
//    @RequestMapping(path = "/usuario/busca/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<UserRepresentation> buscaUsuario (
//            @PathVariable(name = "id") Long id,
//            @RequestHeader(name = "header") HttpHeaders header
//    )  {
//        try {
//            UserRepresentation userRepresentation = keycloak.realm(realm).users().get(Long.toString(id)).toRepresentation();
//            if (userRepresentation != null) {
//                return new ResponseEntity<>(userRepresentation, HttpStatus.OK);
//            }
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//        } catch (Grupo2Exception ex) {
//            throw new HttpException("Erro ao recuperar dados do usuário", EnumError.NOT_FOUND_404);
//        }
//    }


    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza um usuário")
    @RequestMapping(path = "/usuarios/atualiza/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void atualizaUsuario (
            @PathVariable(name = "id") Long id,
            @RequestBody(required = false) AtualizaUsuarioDTO atualizaUsuarioDTO,
            @RequestHeader(name = "header") HttpHeaders header
    ) {
        try {
            var credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(atualizaUsuarioDTO.getPassword());
            var atualizaUsuario = new UserRepresentation();
            atualizaUsuario.setUsername(atualizaUsuarioDTO.getUsername());
            atualizaUsuario.setCredentials(List.of(credentialRepresentation));
            atualizaUsuario.setEnabled(true);
            atualizaUsuario.setFirstName(atualizaUsuarioDTO.getFirstName());
            atualizaUsuario.setLastName(atualizaUsuarioDTO.getLastName());

            keycloak.realm(realm).users().get(Long.toString(id)).update(atualizaUsuario);

        } catch (Grupo2Exception ex) {
            throw new HttpException("Erro ao atualizar usuário", EnumError.NOT_FOUND_404);
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reseta a senha de um usuário")
    @RequestMapping(path = "/usuarios/reseta/{id}", method = RequestMethod.PATCH)
    public void atualizaSenhaUsuario (
            @PathVariable(name = "id") Long id,
            @RequestBody(required = false) AtualizaSenhaUsuarioDTO atualizaSenhaUsuarioDTO,
            @RequestHeader(name = "accessToken") HttpHeaders header
    ) {
        try {
            var usuarioEncontrado = keycloak.realm(realm).users().get(Long.toString(id)).toRepresentation();
            var credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(atualizaSenhaUsuarioDTO.getPassword());
            usuarioEncontrado.setCredentials(List.of(credentialRepresentation));
            usuarioEncontrado.getCredentials().forEach(e -> System.out.println(e.getValue()));

            keycloak.realm(realm).users().get(Long.toString(id)).update(usuarioEncontrado);

        } catch (Grupo2Exception ex) {
            throw new HttpException("Erro ao atualizar a senha!", EnumError.NOT_FOUND_404);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Exclui um usuário")
    @RequestMapping(path = "/usuarios/deleta/{id}", method = RequestMethod.DELETE)
    public void excluiUsuario (
            @PathVariable(name = "id") Long id,
            @RequestHeader(name = "accessToken") HttpHeaders header
    ) {
        try {
            var usuarioEncontrado = keycloak.realm(realm).users().get(Long.toString(id)).toRepresentation();
            usuarioEncontrado.setEnabled(false);

            keycloak.realm(realm).users().get(Long.toString(id)).remove();

        } catch (Grupo2Exception ex) {
            throw new HttpException("Erro ao Deletar!", EnumError.NOT_FOUND_404);
        }
    }


    private UserRepresentation getUsuarioCriado(CriacaoUsuarioDTO usuarioDTO) {
        var credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(usuarioDTO.getPassword());
        var novoUsuario = new UserRepresentation();
        novoUsuario.setUsername(usuarioDTO.getUsername());
        novoUsuario.setCredentials(List.of(credentialRepresentation));
        novoUsuario.setEnabled(true);
        return novoUsuario;
    }
}
