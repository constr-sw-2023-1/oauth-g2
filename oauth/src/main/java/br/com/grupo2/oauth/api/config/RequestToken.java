package br.com.grupo2.oauth.api.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RequestToken {

    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String grantType;
    private String authurl;

    public RequestToken() {}

    public RequestToken(@Value("{$keycloak.resource}") String clientId, @Value("{$keycloak.credentials.secret}") String clientSecret, @Value("{$variables.keycloak.username}") String username, @Value("{$variables.keycloak.password}") String password,
                        @Value("{$spring.security.oauth2.client.registration.okta.authorization-grant-type}") String grantType, @Value("{token.url}") String authurl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.authurl = authurl;
    }

    @Bean
    public Keycloak keyCloak(){
        return KeycloakBuilder.builder()
                .serverUrl(authurl)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(new ResteasyClientBuilder()
                        .connectionPoolSize(10)
                        .build())
                .build();
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public void setAuthurl(String authurl) {
        this.authurl = authurl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getAuthurl() {
        return authurl;
    }

}
