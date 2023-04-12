//package br.com.grupo2.oauth.api.config;
//
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
//import org.keycloak.OAuth2Constants;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.KeycloakBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KeycloakClientConfig {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakClientConfig.class);
//
//    private final String secretKey;
//    private final String clientId;
//    private final String authUrl;
//    private final String realm;
//
//    public KeycloakClientConfig(@Value("${keycloak.credentials.secret}") String secretKey, @Value("${keycloak.resource}") String clientId, @Value("${keycloak.auth-server-url}") String authUrl, @Value("${keycloak.realm}") String realm) {
//        this.secretKey = secretKey;
//        this.clientId = clientId;
//        this.authUrl = authUrl;
//        this.realm = realm;
//    }
//
//    @Bean
//    public Keycloak keyCloak(){
//        return KeycloakBuilder.builder()
//                .serverUrl(authUrl)
//                .realm(realm)
//                .grantType(OAuth2Constants.PASSWORD)
//                .username("admin")
//                .password("admin")
//                .clientId(clientId)
//                .clientSecret(secretKey)
//                .resteasyClient(new ResteasyClientBuilder()
//                        .connectionPoolSize(10)
//                        .build())
//                .build();
//    }
//}
