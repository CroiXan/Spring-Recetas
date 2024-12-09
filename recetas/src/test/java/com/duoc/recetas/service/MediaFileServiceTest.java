package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import com.duoc.recetas.configuration.RecetaConfig;
import com.duoc.recetas.security.TokenStore;

import reactor.core.publisher.Mono;

public class MediaFileServiceTest {

    @InjectMocks
    private MediaFileService mediaFileService;

    @Mock
    private RecetaConfig recetaConfig;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFile() {
        Path filePath = Path.of("test-file.txt");
        String description = "Test File Description";
        Long idReceta = 1L;
        String fakeToken = "fake-token";
        FileSystemResource fileResource = new FileSystemResource(filePath.toFile());

        when(recetaConfig.webClientWithJwt(fakeToken)).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("media/upload")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(MediaType.MULTIPART_FORM_DATA)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn((RequestHeadersSpec) requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("File Uploaded Successfully"));

        mediaFileService.tokenStore.setToken(fakeToken);
        mediaFileService.recetaConfig = recetaConfig;

        Mono<String> result = mediaFileService.uploadFile(filePath, description, idReceta);

        assertNotNull(result);
        assertEquals("File Uploaded Successfully", result.block());

        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri("media/upload");
        verify(requestBodyUriSpec, times(1)).contentType(MediaType.MULTIPART_FORM_DATA);
        verify(requestBodyUriSpec, times(1)).bodyValue(any());
        verify(requestBodyUriSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    void testGetImageUrlForReceta() {
        Long idReceta = 1L;
        String fakeToken = "fake-token";
        byte[] mockImageBytes = new byte[] { 1, 2, 3, 4 };

        when(recetaConfig.webClientWithJwt(fakeToken)).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("media/receta/{id}", idReceta)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(mockImageBytes));

        mediaFileService.tokenStore.setToken(fakeToken);
        mediaFileService.recetaConfig = recetaConfig;

        Mono<String> result = mediaFileService.getImageUrlForReceta(idReceta);

        assertNotNull(result);
        String expectedBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(mockImageBytes);
        assertEquals(expectedBase64, result.block());

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("media/receta/{id}", idReceta);
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(byte[].class);
    }

    @Test
    void testGetToken() throws Exception {
        String fakeResponse = "{\"token\":\"fake-token\"}";
        TokenStore mockTokenStore = new TokenStore();
        mockTokenStore.setToken("fake-token");

        WebClient.Builder webClientBuilderMock = mock(WebClient.Builder.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClientBuilderMock.baseUrl("http://localhost:8082/user/login")).thenReturn(webClientBuilderMock);
        when(webClientBuilderMock.build()).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just(fakeResponse));

        MediaFileService mediaFileService = new MediaFileService(webClientBuilderMock);

        Method getTokenMethod = MediaFileService.class.getDeclaredMethod("getToken");
        getTokenMethod.setAccessible(true);

        getTokenMethod.invoke(mediaFileService);

        assertEquals("fake-token", mediaFileService.tokenStore.getToken());

        verify(webClientBuilderMock, times(1)).baseUrl("http://localhost:8082/user/login");
        verify(webClientMock, times(1)).post();
        verify(requestBodyUriSpecMock, times(1)).bodyValue(any());
        verify(requestHeadersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(String.class);
    }
}
