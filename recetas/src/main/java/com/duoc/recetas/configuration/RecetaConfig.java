package com.duoc.recetas.configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RecetaConfig {

    @Value("${recetaservice.base.url}")
    private String recetaBaseURL;

    @Bean
    public ModelMapper modelMapperBean(){
        return new ModelMapper();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeaders(headers -> {
                String auth = "admin:WpCsGw3jp*";
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                headers.set("Authorization", "Basic " + encodedAuth);
            }).baseUrl(recetaBaseURL).build();
    }
}
