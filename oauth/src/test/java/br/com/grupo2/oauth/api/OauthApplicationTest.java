package br.com.grupo2.oauth.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class OauthApplicationTest {

    @Test
    void testaMetodoMain() {
        OauthApplication.main(new String[] {});
    }
}