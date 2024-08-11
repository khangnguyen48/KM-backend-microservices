package com.nskay.microservices.inventory.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("Inventory Service API")
                        .description("This is RESTful API for Inventory Service")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
//                .externalDocs(new ExternalDocumentation()
//                        .description("You can refer to the Product Service API documentation")
//                        .url("https://inventory-service-dummy-url.com/docs"));
    }
}
