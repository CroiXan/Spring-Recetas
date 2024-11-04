package com.duoc.recetas.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.duoc.recetas.model.UserLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private TokenStore tokenStore;

    private static final String SECRET_KEY = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsb3BhcmFqd3Rjb25zcHJpbmdzZWN1cml0eQ==bWlwcnVlYmFkZWVqbXBsb3BhcmFiYXNlNjQ=";

    public CustomAuthenticationProvider(TokenStore tokenStore) {
        super();
        this.tokenStore = tokenStore;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("Llegué Custom Authentication Provider: ");
        System.out.println("Authentication: " + authentication);
        final String name = authentication.getName();
        System.out.println("Name: " + name);
        final String password = authentication.getCredentials().toString();
        System.out.println("Password: " + password);

        System.out.println("Custom Authentication Provider: " + name);
        //log.info("Login Success");


        final MultiValueMap requestBody = new LinkedMultiValueMap<>();
        requestBody.add("user", name);
        requestBody.add("encryptedPass", password);

        System.out.println("Request Body: " + requestBody);

        final var restTemplate = new RestTemplate();
        //final var responseEntity = restTemplate.postForEntity("http://localhost:8082/auth/login", requestBody, String.class);

        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8082/user/login").build();
        UserLogin loginRequest = new UserLogin();
        loginRequest.setEmail(name);
        loginRequest.setPassword(password);
        String response = webClient.post()
            .bodyValue(loginRequest)
            .retrieve()
            .bodyToMono(String.class)
            .switchIfEmpty(Mono.empty())
            .block();

        Gson gson = new Gson();
        TokenStore tokenResponse = gson.fromJson(response, TokenStore.class);

        Map<String, Object> claims = decodeJWT(tokenResponse.getToken());

        String role = (String) claims.get("role");
        System.out.println("Token role: " + role);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" +role));

        System.out.println("Response Entity: " + tokenResponse.getToken());//responseEntity);

        tokenStore.setToken(tokenResponse.getToken());//responseEntity.getBody());

        System.out.println("Token Store: " + tokenStore.getToken());

        //if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // new BadCredentialsException("Invalid username or password");
        //}

        Authentication authenticatedToken = new UsernamePasswordAuthenticationToken(name, password, authorities);

        return authenticatedToken;
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Map<String, Object> decodeJWT(String token) {
    String[] parts = token.split("\\."); // Divide el JWT en sus tres partes
    String payload = parts[1]; // El payload es la segunda parte

    // Decodifica el payload de Base64 a una cadena JSON
    byte[] decodedBytes = Base64.getDecoder().decode(payload);
    String decodedPayload = new String(decodedBytes);

    // Convierte la cadena JSON en un mapa para acceder a los claims
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> claims = null;
    try {
        claims = mapper.readValue(decodedPayload, Map.class);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return claims;
}
}
