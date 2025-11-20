package com.azki.reservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI azkiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Azki Reservation API")
                        .description("AzkiReservation")
                        .version("0.0.1"));
    }
}
