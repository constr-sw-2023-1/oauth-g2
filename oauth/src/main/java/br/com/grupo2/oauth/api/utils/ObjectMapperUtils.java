package br.com.grupo2.oauth.api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtils {

    public <T> Object getMapper (T objeto, T objetoParaConverter) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(objeto, objetoParaConverter.getClass());
    }
}
