package com.duoc.recetas.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class CustomAuthenticationProviderTest {

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Mock
    private TokenStore tokenStore;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testAuthenticate_Success() {
        String username = "testuser";
        String password = "testpass";
        String fakeToken = "fake.jwt.token";

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{\"token\":\"" + fakeToken + "\"}"));

        customAuthenticationProvider = spy(customAuthenticationProvider);
        doReturn(Map.of("role", "USER")).when(customAuthenticationProvider).decodeJWT(fakeToken);

        doNothing().when(tokenStore).setToken(anyString());

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);

        Authentication result = customAuthenticationProvider.authenticate(auth);

        assertNotNull(result);
        assertEquals(username, result.getName());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).bodyValue(any());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
        verify(tokenStore, times(1)).setToken(fakeToken);
    }

    @Test
    void testAuthenticate_Failure() {
        String username = "invaliduser";
        String password = "invalidpass";

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);

        Exception exception = assertThrows(Exception.class, () -> {
            customAuthenticationProvider.authenticate(auth);
        });

        assertNotNull(exception);

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).bodyValue(any());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    void testDecodeJWT_Success() {
        String fakeJWT = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiJ9.signature";

        Map<String, Object> claims = customAuthenticationProvider.decodeJWT(fakeJWT);

        assertNotNull(claims);
        assertEquals("USER", claims.get("role"));
    }

    @Test
    void testDecodeJWT_InvalidToken() {
        String invalidJWT = "invalid.jwt.token";

        Map<String, Object> claims = customAuthenticationProvider.decodeJWT(invalidJWT);

        assertNull(claims);
    }
}
