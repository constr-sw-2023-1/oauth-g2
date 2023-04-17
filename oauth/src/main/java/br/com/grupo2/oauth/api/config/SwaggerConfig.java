package br.com.grupo2.oauth.api.config;

import br.com.grupo2.oauth.api.controller.LoginController;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.titulo}")
    private String titulo;

    @Value("${swagger.versao}")
    private String versao;

    @Value("${swagger.descricao}")
    private String descricao;

    @Bean
    public Docket detalheApi(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2).additionalModels(
                typeResolver.resolve(LoginController.class)
                )
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .apiInfo(informacoesApi());
    }

    private List<ResponseMessage> responseMessages() {
        return new ArrayList<ResponseMessage>() {{
            add(new ResponseMessageBuilder()
                    .code(500)
                    .message("500 message")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(403)
                    .message("Forbidden! (Sem permissão)")
                    .build());
        }};
    }

    private ApiInfo informacoesApi() {
        return new ApiInfoBuilder()
                .contact(new Contact("GRUPO-2", "", ""))
                .title(this.titulo)
                .description(this.descricao)
                .version(this.versao)
                .build();
    }
}
