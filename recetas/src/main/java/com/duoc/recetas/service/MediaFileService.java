package com.duoc.recetas.service;

import java.nio.file.Path;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.duoc.recetas.configuration.RecetaConfig;
import com.duoc.recetas.model.UserLogin;
import com.duoc.recetas.security.TokenStore;
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@Service
public class MediaFileService {

    private TokenStore tokenStore = new TokenStore();

    private RecetaConfig recetaConfig = new RecetaConfig();

    public MediaFileService() {
        tokenStore.setToken("");
    }

    public Mono<String> uploadFile(Path filePath, String description, Long idReceta) {
        FileSystemResource fileResource = new FileSystemResource(filePath.toFile());
        getToken();
        WebClient webClient = recetaConfig.webClientWithJwt(tokenStore.getToken());
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", fileResource, MediaType.MULTIPART_FORM_DATA);
        bodyBuilder.part("description", description);
        bodyBuilder.part("id_receta", idReceta);
        
        try {
            return webClient.post()
                    .uri("media/upload")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(bodyBuilder.build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .switchIfEmpty(Mono.empty());
        } catch (WebClientResponseException e) {
            System.err.println("Error response: " + e.getResponseBodyAsString());
            throw e;
        }

    }

    private void getToken() {
        if (this.tokenStore.getToken().isEmpty()) {
            WebClient webClient = WebClient.builder().baseUrl("http://localhost:8082/user/login").build();
            UserLogin loginRequest = new UserLogin();
            loginRequest.setEmail("test");
            loginRequest.setPassword("test");
            String response = webClient.post()
                    .bodyValue(loginRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .switchIfEmpty(Mono.empty())
                    .block();

            Gson gson = new Gson();
            this.tokenStore = gson.fromJson(response, TokenStore.class);
        }
    }

    private record WebClientRequest(FileSystemResource file, String description, Long idReceta) {
    }

}
