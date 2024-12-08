package com.duoc.recetas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        assertEquals("Carne Mechada", receta.getNombre());
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
        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));

        Flux<RecetaResponse> response = recetaService.getAllRecetaService();
        assertNotNull(response);
        verify(webClient, times(1)).get();
    }

    @Test
    void testGetRecetasByNameService() {
        String name = "Carne Mechada";
        when(webClient.post()).thenReturn(mock(WebClient.RequestBodyUriSpec.class));

        Flux<RecetaResponse> response = recetaService.getRecetasByNameService(name);
        assertNotNull(response);
        verify(webClient, times(1)).post();
    }

    @Test
    void testAddReceta() {
        RecetaRequest request = new RecetaRequest();
        when(webClient.post()).thenReturn(mock(WebClient.RequestBodyUriSpec.class));

        Mono<RecetaRequest> response = recetaService.addReceta(request);
        assertNotNull(response);
        verify(webClient, times(1)).post();
    }

    @Test
    void testGetRecetaById() {
        Long id = 1L;
        when(webClient.get()).thenReturn(mock(WebClient.RequestHeadersUriSpec.class));

        Mono<RecetaResponse> response = recetaService.getRecetaById(id);
        assertNotNull(response);
        verify(webClient, times(1)).get();
    }
    
}
