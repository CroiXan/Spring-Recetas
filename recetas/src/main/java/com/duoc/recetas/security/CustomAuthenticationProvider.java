package com.duoc.recetas.security;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gson.Gson;

import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private TokenStore tokenStore;

    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

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

        System.out.println("Response Entity: " + tokenResponse.getToken());//responseEntity);

        tokenStore.setToken(tokenResponse.getToken());//responseEntity.getBody());

        System.out.println("Token Store: " + tokenStore.getToken());

        //if (responseEntity.getStatusCode() != HttpStatus.OK) {
            // new BadCredentialsException("Invalid username or password");
        //}

        List authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        /*Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(tokenStore.getToken())
                    .getBody();

            // Obtener el rol del claim
        String role = claims.get("role", String.class); // 'role' es el nombre del claim que contiene el rol

            // Crear la lista de autoridades
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));*/

        Authentication authenticatedToken = new UsernamePasswordAuthenticationToken(name, password, authorities);

        return authenticatedToken;
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
