package br.com.grupo2.oauth.api.service.auth;

import br.com.grupo2.oauth.api.config.RefreshToken;
import br.com.grupo2.oauth.api.config.RequestToken;
import br.com.grupo2.oauth.api.config.exception.Grupo2Exception;
import br.com.grupo2.oauth.api.config.exception.HttpException;
import br.com.grupo2.oauth.api.constants.EnumError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AutenticacaoService {


    public HttpResponse<?> requestTokenLogin(RequestToken requestToken, String contentType) {

        try {
            String form = getDados(requestToken).entrySet()
                    .stream().map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI("http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token"))
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofString(form, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;

        } catch (Grupo2Exception ex) {
            throw new HttpException("Error Http!", EnumError.NOT_FOUND_404);
        } catch (URISyntaxException | IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, String> getDados(RequestToken requestToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", requestToken.getClientId());
        parameters.put("client_secret", requestToken.getClientSecret());
        parameters.put("username", requestToken.getUsername());
        parameters.put("password", requestToken.getPassword());
        parameters.put("grant_type", requestToken.getGrantType());
        return parameters;
    }
}

