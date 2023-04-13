package br.com.grupo2.oauth.api.DTO;

public class ExcluiUsuarioDTO {

    private String bearer;

    public ExcluiUsuarioDTO() {}

    public ExcluiUsuarioDTO(String bearer) {
        this.bearer = bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getBearer() {
        return bearer;
    }
}
