package br.com.grupo2.oauth.api.DTO;

public class RecuperaUsuarioDTO {

    private String bearer;

    public RecuperaUsuarioDTO() {}

    public RecuperaUsuarioDTO(String bearer) {
        this.bearer = bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getBearer() {
        return bearer;
    }
}
