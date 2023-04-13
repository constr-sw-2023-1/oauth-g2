package br.com.grupo2.oauth.api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtualizaSenhaUsuarioDTO {

    private String bearer;
    @JsonProperty("password")
    private String password;

    public AtualizaSenhaUsuarioDTO() {}

    public AtualizaSenhaUsuarioDTO(String password, String bearer) {
        this.password = password;
        this.bearer = bearer;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getBearer() {
        return bearer;
    }
}
