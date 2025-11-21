package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mutant Detector API")
                        .version("1.0.0")
                        .description("API para detectar mutantes mediante análisis de secuencias de ADN. " +
                                "Un humano es mutante si se encuentran más de una secuencia de cuatro letras " +
                                "iguales de forma oblicua, horizontal o vertical.")
                        .contact(new Contact()
                                .name("MercadoLibre")
                                .url("https://www.mercadolibre.com")));
    }
}