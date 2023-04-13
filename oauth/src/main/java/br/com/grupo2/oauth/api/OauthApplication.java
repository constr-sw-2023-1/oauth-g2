package br.com.grupo2.oauth.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
public class OauthApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(OauthApplication.class);
		application.run(args);
		System.out.println("SUBIU ----------------------- SUBIU ----------------------- SUBIU ");
	}

}
