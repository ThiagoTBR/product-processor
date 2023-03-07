package br.com.productprocessor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
        	       .select()
                   .apis(RequestHandlerSelectors.basePackage("br.com.productprocessor.controllers"))
                   .paths(PathSelectors.any())
                   .build()
                   .apiInfo(apiInfo());
       }

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Processador e calculador de produtos")
                .description("Aplicação para processamento de produtos a partir de arquivos Json e calculo da divisão dos produtos entre lojistas.")
                .version("1.0.0")
                .build();
    }
	
}