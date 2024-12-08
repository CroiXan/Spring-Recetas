package com.example.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.users.exception.ResourceNotFoundException;
import com.example.users.models.FullReceta;
import com.example.users.models.NameRequest;
import com.example.users.models.Receta;
import com.example.users.service.RecetaService;

public class RecetaControllerTest {

    @InjectMocks
    private RecetaController recetaController;

    @Mock
    private RecetaService recetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReceta_ShouldReturnListOfRecetas() {
        List<FullReceta> recetas = Arrays.asList(new FullReceta(), new FullReceta());
        when(recetaService.getAllReceta()).thenReturn(recetas);

        ResponseEntity<List<FullReceta>> response = recetaController.getAllReceta();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recetas, response.getBody());
        verify(recetaService, times(1)).getAllReceta();
    }

    @Test
    void createReceta_ShouldReturnCreatedReceta() {
        Receta receta = new Receta();
        Receta newReceta = new Receta();
        when(recetaService.saveReceta(receta)).thenReturn(newReceta);

        ResponseEntity<Receta> response = recetaController.createReceta(receta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newReceta, response.getBody());
        verify(recetaService, times(1)).saveReceta(receta);
    }

    @Test
    void getReceta_ShouldReturnReceta() {
        FullReceta receta = new FullReceta();
        when(recetaService.getFullRecetabyId(1L)).thenReturn(Optional.of(receta));

        ResponseEntity<FullReceta> response = recetaController.getReceta(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(receta, response.getBody());
        verify(recetaService, times(1)).getFullRecetabyId(1L);
    }

    @Test
    void getReceta_ShouldThrowExceptionWhenNotFound() {
        when(recetaService.getFullRecetabyId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recetaController.getReceta(1L));

        verify(recetaService, times(1)).getFullRecetabyId(1L);
    }

    @Test
    void deleteReceta_ShouldReturnNoContent() {
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));

        ResponseEntity<Void> response = recetaController.deleteReceta(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(recetaService, times(1)).deleteRecetaById(1L);
    }

    @Test
    void deleteReceta_ShouldThrowExceptionWhenNotFound() {
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recetaController.deleteReceta(1L));

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(recetaService, never()).deleteRecetaById(any());
    }

    @Test
    void updateReceta_ShouldReturnUpdatedReceta() {
        Receta existingReceta = new Receta();
        Receta updatedReceta = new Receta();
        updatedReceta.setNombre("Updated Name");
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(existingReceta));
        when(recetaService.saveReceta(existingReceta)).thenReturn(existingReceta);

        ResponseEntity<Receta> response = recetaController.updateReceta(1L, updatedReceta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingReceta, response.getBody());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(recetaService, times(1)).saveReceta(existingReceta);
    }

    @Test
    void updateReceta_ShouldThrowExceptionWhenNotFound() {
        Receta updatedReceta = new Receta();
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recetaController.updateReceta(1L, updatedReceta));

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(recetaService, never()).saveReceta(any());
    }

    @Test
    void findRecetaByName_ShouldReturnListOfRecetas() {
        NameRequest request = new NameRequest();
        request.setName("Test Name");
        List<FullReceta> recetas = Arrays.asList(new FullReceta(), new FullReceta());
        when(recetaService.findRecetaByName("Test Name")).thenReturn(recetas);

        ResponseEntity<List<FullReceta>> response = recetaController.findRecetaByName(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recetas, response.getBody());
        verify(recetaService, times(1)).findRecetaByName("Test Name");
    }

}
