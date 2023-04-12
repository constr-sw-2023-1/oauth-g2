package br.com.grupo2.oauth.api.config.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AutenticacaoUtils {

    public class AuthUtils {
        public static String getUsuarioToken() {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getHeader("Authorization");
        }
    }
}
