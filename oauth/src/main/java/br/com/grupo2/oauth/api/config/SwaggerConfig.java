package br.com.grupo2.oauth.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
//@EnableSwagger2
public class SwaggerConfig /*implements WebMvcConfigurer*/ {

    @Value("${swagger.titulo}")
    private String titulo;

    @Value("${swagger.versao}")
    private String versao;

    @Value("${swagger.descricao}")
    private String descricao;

    @Bean
    public Docket detalheApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(informacoesApi());
    }

    private ApiInfo informacoesApi() {
        return new ApiInfoBuilder().title(this.titulo)
                .version(this.versao).description(this.descricao).build();
    }

//    private final String baseUrl;
//
//    public SwaggerConfig(@Value("${application.base.url}") String baseUrl) {
//        this.baseUrl = baseUrl;
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController(baseUrl + "/swagger-ui/")
//                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
//    }
}
