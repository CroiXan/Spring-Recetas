package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import com.duoc.recetas.configuration.RecetaConfig;
import com.duoc.recetas.model.IngredienteResponse;
import com.duoc.recetas.model.InstruccionResponse;
import com.duoc.recetas.model.NameRequest;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaRequest;
import com.duoc.recetas.model.RecetaResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecetaServiceTest {

    @InjectMocks
    private RecetaService recetaService;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerRecetas() {
        List<Receta> recetas = recetaService.obtenerRecetas();
        assertNotNull(recetas);
        assertEquals(3, recetas.size());
    }

    @Test
    void testCrearReceta() {
        Receta nuevaReceta = new Receta(null, "Nueva Receta", List.of("Ingrediente1"), List.of(), "");
        recetaService.crearReceta(nuevaReceta);

        List<Receta> recetas = recetaService.obtenerRecetas();
        assertEquals(4, recetas.size());
        assertEquals("Nueva Receta", recetas.get(3).getNombre());
    }

    @Test
    void testObtenerRecetaPorId() {
        Receta receta = recetaService.obtenerRecetaPorId(1L);
        assertNotNull(receta);
        assertEquals("Carne Mechada ", receta.getNombre());
    }

    @Test
    void testBorrarReceta() {
        recetaService.borrarReceta(1L);
        Receta receta = recetaService.obtenerRecetaPorId(1L);
        assertNull(receta);
    }

    @Test
    void testObtenerRecetaPorNombre() {
        List<Receta> recetas = recetaService.obtenerRecetaPorNombre("Pastel");
        assertEquals(2, recetas.size());
    }

    @Test
    void testGetAllRecetaService() {

        String fakeToken = "fake-token";
        RecetaResponse recetaResponseMock = new RecetaResponse();
        recetaResponseMock.setNombre("Carne Mechada");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);

        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("receta")).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(RecetaResponse.class))
                .thenReturn(Flux.just(recetaResponseMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Flux<RecetaResponse> result = recetaService.getAllRecetaService();

        assertNotNull(result);
        List<RecetaResponse> responseList = result.collectList().block();
        assertEquals(1, responseList.size());
        assertEquals("Carne Mechada", responseList.get(0).getNombre());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).get();
        verify(requestHeadersUriSpecMock, times(1)).uri("receta");
        verify(requestHeadersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToFlux(RecetaResponse.class);
    }

    @Test
    void testGetRecetasByNameService() {
        String name = "Carne Mechada";
        String fakeToken = "fake-token";
        RecetaResponse recetaResponseMock = new RecetaResponse();
        recetaResponseMock.setNombre(name);

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);

        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("receta/name")).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(any(NameRequest.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(RecetaResponse.class))
                .thenReturn(Flux.just(recetaResponseMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Flux<RecetaResponse> result = recetaService.getRecetasByNameService(name);

        assertNotNull(result);
        List<RecetaResponse> responseList = result.collectList().block();
        assertEquals(1, responseList.size());
        assertEquals("Carne Mechada", responseList.get(0).getNombre());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).post();
        verify(requestBodyUriSpecMock, times(1)).uri("receta/name");
        verify(requestBodyUriSpecMock, times(1)).bodyValue(any(NameRequest.class));
        verify(requestHeadersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToFlux(RecetaResponse.class);
    }

    @Test
    void testAddReceta() {
        String fakeToken = "fake-token";
        RecetaRequest recetaRequestMock = new RecetaRequest();
        recetaRequestMock.setNombre("Nueva Receta");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("receta")).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(recetaRequestMock)).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(RecetaRequest.class)).thenReturn(Mono.just(recetaRequestMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Mono<RecetaRequest> result = recetaService.addReceta(recetaRequestMock);

        assertNotNull(result);
        RecetaRequest response = result.block();
        assertEquals("Nueva Receta", response.getNombre());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).post();
        verify(requestBodyUriSpecMock, times(1)).uri("receta");
        verify(requestBodyUriSpecMock, times(1)).bodyValue(recetaRequestMock);
        verify(requestHeadersSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(RecetaRequest.class);
    }

    @Test
    void testGetRecetaById() {
        Long recetaId = 1L;
        String fakeToken = "fake-token";
        RecetaResponse recetaResponseMock = new RecetaResponse();
        recetaResponseMock.setNombre("Receta de Prueba");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("receta/{id}", recetaId)).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(RecetaResponse.class)).thenReturn(Mono.just(recetaResponseMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Mono<RecetaResponse> result = recetaService.getRecetaById(recetaId);

        assertNotNull(result);
        RecetaResponse response = result.block();
        assertEquals("Receta de Prueba", response.getNombre());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).get();
        verify(requestHeadersUriSpecMock, times(1)).uri("receta/{id}", recetaId);
        verify(requestHeadersUriSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(RecetaResponse.class);
    }

    @Test
    void testEditarReceta() {
        RecetaResponse recetaMock = new RecetaResponse();
        recetaMock.setId_receta(1L);
        recetaMock.setNombre("Receta Editada");

        String fakeToken = "fake-token";

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("receta/{id}", recetaMock.getId_receta())).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(any(RecetaRequest.class)))
                .thenReturn((RequestHeadersSpec) requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(RecetaResponse.class)).thenReturn(Mono.just(recetaMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.editarReceta(recetaMock);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).put();
        verify(requestBodyUriSpecMock, times(1)).uri("receta/{id}", recetaMock.getId_receta());
        verify(requestBodyUriSpecMock, times(1)).bodyValue(any(RecetaRequest.class));
        verify(requestBodyUriSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(RecetaResponse.class);
    }

    @Test
    void testEliminarIngrediente() {
        Long ingredienteId = 1L;
        String fakeToken = "fake-token";

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.delete()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("ingredientes/{id}", ingredienteId)).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Void.class)).thenReturn(Mono.empty());

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.eliminarIngrediente(ingredienteId);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).delete();
        verify(requestHeadersUriSpecMock, times(1)).uri("ingredientes/{id}", ingredienteId);
        verify(requestHeadersUriSpecMock, times(1)).retrieve();
    }

    @Test
    void testGetInstruccionById() {
        Long instruccionId = 1L;
        String fakeToken = "fake-token";
        InstruccionResponse instruccionResponseMock = new InstruccionResponse();
        instruccionResponseMock.setDescripcion("Descripción de prueba");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("instrucciones/{id}", instruccionId)).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InstruccionResponse.class)).thenReturn(Mono.just(instruccionResponseMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Mono<InstruccionResponse> result = recetaService.getInstruccionById(instruccionId);

        assertNotNull(result);
        InstruccionResponse response = result.block();
        assertEquals("Descripción de prueba", response.getDescripcion());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).get();
        verify(requestHeadersUriSpecMock, times(1)).uri("instrucciones/{id}", instruccionId);
        verify(requestHeadersUriSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(InstruccionResponse.class);
    }

    @Test
    void testGetIngredienteById() {
        Long ingredienteId = 1L;
        String fakeToken = "fake-token";
        IngredienteResponse ingredienteResponseMock = new IngredienteResponse();
        ingredienteResponseMock.setNombr_item("Ingrediente Prueba");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("ingredientes/{id}", ingredienteId)).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(IngredienteResponse.class)).thenReturn(Mono.just(ingredienteResponseMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Mono<IngredienteResponse> result = recetaService.getIngredienteById(ingredienteId);

        assertNotNull(result);
        IngredienteResponse response = result.block();
        assertEquals("Ingrediente Prueba", response.getNombr_item());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).get();
        verify(requestHeadersUriSpecMock, times(1)).uri("ingredientes/{id}", ingredienteId);
        verify(requestHeadersUriSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(IngredienteResponse.class);
    }

    @Test
    void testAddIngrediente() {
        String fakeToken = "fake-token";
        IngredienteResponse ingredienteRequestMock = new IngredienteResponse();
        ingredienteRequestMock.setNombr_item("Ingrediente Nuevo");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("ingredientes")).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(ingredienteRequestMock))
                .thenReturn((RequestHeadersSpec) requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(IngredienteResponse.class)).thenReturn(Mono.just(ingredienteRequestMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        Mono<IngredienteResponse> result = recetaService.addIngrediente(ingredienteRequestMock);

        assertNotNull(result);
        IngredienteResponse response = result.block();
        assertEquals("Ingrediente Nuevo", response.getNombr_item());

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).post();
        verify(requestBodyUriSpecMock, times(1)).uri("ingredientes");
        verify(requestBodyUriSpecMock, times(1)).bodyValue(ingredienteRequestMock);
        verify(responseSpecMock, times(1)).bodyToMono(IngredienteResponse.class);
    }

    @Test
    void testEditarIngrediente() {
        String fakeToken = "fake-token";
        IngredienteResponse ingredienteRequestMock = new IngredienteResponse();
        ingredienteRequestMock.setId_ingrediente(1L);
        ingredienteRequestMock.setNombr_item("Ingrediente Editado");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("ingredientes/{id}", ingredienteRequestMock.getId_ingrediente()))
                .thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(ingredienteRequestMock))
                .thenReturn((RequestHeadersSpec) requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(IngredienteResponse.class)).thenReturn(Mono.just(ingredienteRequestMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.editarIngrediente(ingredienteRequestMock);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).put();
        verify(requestBodyUriSpecMock, times(1)).uri("ingredientes/{id}", ingredienteRequestMock.getId_ingrediente());
        verify(requestBodyUriSpecMock, times(1)).bodyValue(ingredienteRequestMock);
        verify(responseSpecMock, times(1)).bodyToMono(IngredienteResponse.class);
    }

    @Test
    void testEditarInstruccion() {
        String fakeToken = "fake-token";
        InstruccionResponse instruccionRequestMock = new InstruccionResponse();
        instruccionRequestMock.setId_instruccion(1L);
        instruccionRequestMock.setDescripcion("Instrucción Editada");

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("instrucciones/{id}", instruccionRequestMock.getId_instruccion()))
                .thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(instruccionRequestMock))
                .thenReturn((RequestHeadersSpec) requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InstruccionResponse.class)).thenReturn(Mono.just(instruccionRequestMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.editarInstruccion(instruccionRequestMock);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).put();
        verify(requestBodyUriSpecMock, times(1)).uri("instrucciones/{id}", instruccionRequestMock.getId_instruccion());
        verify(requestBodyUriSpecMock, times(1)).bodyValue(instruccionRequestMock);
    }

    @Test
    void testEliminarInstruccion() {
        Long instruccionId = 1L;
        String fakeToken = "fake-token";

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.delete()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("instrucciones/{id}", instruccionId)).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Void.class)).thenReturn(Mono.empty());

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.eliminarInstruccion(instruccionId);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).delete();
        verify(requestHeadersUriSpecMock, times(1)).uri("instrucciones/{id}", instruccionId);
        verify(requestHeadersUriSpecMock, times(1)).retrieve();
        verify(responseSpecMock, times(1)).bodyToMono(Void.class);
    }

    @Test
    void testAgregarInstruccion() {
        Long recetaId = 1L;
        String descripcion = "Nueva Instrucción";
        int posicion = 1;
        InstruccionResponse instruccionRequestMock = new InstruccionResponse();
        instruccionRequestMock.setId_instruccion(recetaId);
        instruccionRequestMock.setDescripcion(descripcion);
        instruccionRequestMock.setPosicion(posicion);
        String fakeToken = "fake-token";

        RecetaConfig recetaConfigMock = mock(RecetaConfig.class);
        WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(recetaConfigMock.webClientWithJwt(fakeToken)).thenReturn(webClientMock);
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri("instrucciones")).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(any(InstruccionResponse.class)))
                .thenReturn((RequestHeadersSpec) requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InstruccionResponse.class)).thenReturn(Mono.just(instruccionRequestMock));

        recetaService.tokenStore.setToken(fakeToken);
        recetaService.recetaConfig = recetaConfigMock;

        recetaService.agregarInstruccion(recetaId, descripcion, posicion);

        verify(recetaConfigMock, times(1)).webClientWithJwt(fakeToken);
        verify(webClientMock, times(1)).post();
        verify(requestBodyUriSpecMock, times(1)).uri("instrucciones");
        verify(requestBodyUriSpecMock, times(1)).bodyValue(any(InstruccionResponse.class));
    }
}
