package com.duoc.recetas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.duoc.recetas.configuration.RecetaConfig;
import com.duoc.recetas.model.UserLogin;
import com.duoc.recetas.security.TokenStore;
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@Service
public class MediaFileService {

    private TokenStore tokenStore = new TokenStore();

    private RecetaConfig recetaConfig = new RecetaConfig();

    public MediaFileService(){
        tokenStore.setToken("");
    }

    private void getToken(){
        if(this.tokenStore.getToken().isEmpty()){
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

}
