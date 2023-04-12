package br.com.grupo2.oauth.api.service.auth;

import br.com.grupo2.oauth.api.config.RequestToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class AutenticacaoService {

    public void requestTokenLogin(RequestToken requestToken, String contentType) throws Exception {

        String url = "http://localhost:8090/auth/realms/Construc-sw-2023-1/protocol/openid-connect/token";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header

        con.setRequestProperty("Content-Type", contentType);


        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", requestToken.getClientId());
        parameters.put("client_secret", requestToken.getClientSecret());
        parameters.put("username", requestToken.getUsername());
        parameters.put("password", requestToken.getPassword());
        parameters.put("grant_type", requestToken.getGrantType());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        // send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postDataBytes);
        }

        // read response
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        log.info(responseCode);
    }
}

