package com.duoc.recetas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.service.MediaFileService;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecetasControllerTest {

    @InjectMocks
    private RecetasController recetasController;

    @Mock
    private RecetaService recetaService;

    @Mock
    private MediaFileService mediaFileService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecetas() {
        // Datos simulados
        List<RecetaResponse> mockRecetas = new ArrayList<>();
        RecetaResponse receta1 = new RecetaResponse();
        receta1.setId_receta(1L);
        mockRecetas.add(receta1);

        Map<Long, String> mockImagenes = new HashMap<>();
        mockImagenes.put(1L, "http://image.url/receta1.jpg");

        // Configuración de mocks
        when(recetaService.getAllRecetaService()).thenReturn(Flux.fromIterable(mockRecetas));
        when(mediaFileService.getImageUrlForReceta(1L)).thenReturn(Mono.just("http://image.url/receta1.jpg"));

        // Ejecución del método
        String viewName = recetasController.recetas(model);

        // Verificaciones
        assertEquals("recetas", viewName);
        verify(model, times(1)).addAttribute("imagenes", mockImagenes);
        verify(model, times(1)).addAttribute("listarecetas", mockRecetas);
        verify(recetaService, times(1)).getAllRecetaService();
        verify(mediaFileService, times(1)).getImageUrlForReceta(1L);
    }

    @Test
    void testBusqueda() {
        // Datos simulados
        String searchQuery = "tarta";
        List<RecetaResponse> mockRecetas = new ArrayList<>();
        RecetaResponse receta1 = new RecetaResponse();
        receta1.setNombre("Tarta de Manzana");
        mockRecetas.add(receta1);

        // Configuración de mocks
        when(recetaService.getRecetasByNameService(searchQuery)).thenReturn(Flux.fromIterable(mockRecetas));

        // Ejecución del método
        String viewName = recetasController.busqueda(model, searchQuery);

        // Verificaciones
        assertEquals("busqueda", viewName);
        verify(model, times(1)).addAttribute("listarecetas", mockRecetas);
        verify(model, times(1)).addAttribute("search", searchQuery);
        verify(recetaService, times(1)).getRecetasByNameService(searchQuery);
    }

    @Test
    void testRaiz() {
        // Datos simulados
        List<RecetaResponse> mockRecetas = new ArrayList<>();
        RecetaResponse receta1 = new RecetaResponse();
        receta1.setId_receta(1L);
        mockRecetas.add(receta1);

        Map<Long, String> mockImagenes = new HashMap<>();
        mockImagenes.put(1L, "http://image.url/receta1.jpg");

        // Configuración de mocks
        when(recetaService.getAllRecetaService()).thenReturn(Flux.fromIterable(mockRecetas));
        when(mediaFileService.getImageUrlForReceta(1L)).thenReturn(Mono.just("http://image.url/receta1.jpg"));

        // Ejecución del método
        String viewName = recetasController.raiz(model);

        // Verificaciones
        assertEquals("recetas", viewName);
        verify(model, times(1)).addAttribute("imagenes", mockImagenes);
        verify(model, times(1)).addAttribute("listarecetas", mockRecetas);
        verify(recetaService, times(1)).getAllRecetaService();
        verify(mediaFileService, times(1)).getImageUrlForReceta(1L);
    }
}
