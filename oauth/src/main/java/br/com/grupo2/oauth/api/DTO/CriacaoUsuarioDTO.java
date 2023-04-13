package br.com.grupo2.oauth.api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriacaoUsuarioDTO {

    private String bearerToken;
    @JsonProperty("username")
    public String username;
    @JsonProperty("password")
    public String password;
    @JsonProperty("first-name")
    public String firstName;
    @JsonProperty("last-name")
    public String lastName;

    public CriacaoUsuarioDTO() {}

    public CriacaoUsuarioDTO(String bearerToken, String username, String password, String firstName, String lastName) {
        this.bearerToken = bearerToken;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
