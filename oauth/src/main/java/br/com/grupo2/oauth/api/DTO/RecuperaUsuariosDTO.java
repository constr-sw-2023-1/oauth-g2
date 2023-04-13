package br.com.grupo2.oauth.api.DTO;

public class RecuperaUsuariosDTO {

    private String bearer;

    public RecuperaUsuariosDTO() {}

    public RecuperaUsuariosDTO(String bearer) {
        this.bearer = bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getBearer() {
        return bearer;
    }
}
