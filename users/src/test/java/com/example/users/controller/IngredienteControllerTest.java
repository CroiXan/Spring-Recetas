package com.example.users.controller;

import com.example.users.exception.ResourceNotFoundException;
import com.example.users.models.Ingrediente;
import com.example.users.models.Receta;
import com.example.users.service.IngredienteService;
import com.example.users.service.RecetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IngredienteControllerTest {

    @InjectMocks
    private IngredienteController ingredienteController;

    @Mock
    private IngredienteService ingredienteService;

    @Mock
    private RecetaService recetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllIngredientes_ShouldReturnListOfIngredientes() {
        List<Ingrediente> ingredientes = Arrays.asList(new Ingrediente(), new Ingrediente());
        when(ingredienteService.getAllIngrediente()).thenReturn(ingredientes);

        ResponseEntity<List<Ingrediente>> response = ingredienteController.getAllIngredientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredientes, response.getBody());
        verify(ingredienteService, times(1)).getAllIngrediente();
    }

    @Test
    void createIngrediente_ShouldReturnCreatedIngrediente() {
        Ingrediente newIngrediente = new Ingrediente();
        newIngrediente.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(ingredienteService.saveIngrediente(newIngrediente)).thenReturn(newIngrediente);

        ResponseEntity<Ingrediente> response = ingredienteController.createIngrediente(newIngrediente);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newIngrediente, response.getBody());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(ingredienteService, times(1)).saveIngrediente(newIngrediente);
    }

    @Test
    void createIngrediente_ShouldThrowExceptionWhenRecetaNotFound() {
        Ingrediente newIngrediente = new Ingrediente();
        newIngrediente.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            ingredienteController.createIngrediente(newIngrediente);
        });

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(ingredienteService, never()).saveIngrediente(any());
    }

    @Test
    void getIngrediente_ShouldReturnIngrediente() {
        Ingrediente ingrediente = new Ingrediente();
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.of(ingrediente));

        ResponseEntity<Ingrediente> response = ingredienteController.getIngrediente(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingrediente, response.getBody());
        verify(ingredienteService, times(1)).getIngredienteById(1L);
    }

    @Test
    void getIngrediente_ShouldThrowExceptionWhenNotFound() {
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            ingredienteController.getIngrediente(1L);
        });

        verify(ingredienteService, times(1)).getIngredienteById(1L);
    }

    @Test
    void deleteIngrediente_ShouldReturnNoContent() {
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.of(new Ingrediente()));

        ResponseEntity<Void> response = ingredienteController.deleteIngrediente(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ingredienteService, times(1)).getIngredienteById(1L);
        verify(ingredienteService, times(1)).deleteIngredienteById(1L);
    }

    @Test
    void deleteIngrediente_ShouldThrowExceptionWhenNotFound() {
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            ingredienteController.deleteIngrediente(1L);
        });

        verify(ingredienteService, times(1)).getIngredienteById(1L);
        verify(ingredienteService, never()).deleteIngredienteById(any());
    }

    @Test
    void updateIngrediente_ShouldReturnUpdatedIngrediente() {
        Ingrediente existingIngrediente = new Ingrediente();
        Ingrediente updatedIngrediente = new Ingrediente();
        updatedIngrediente.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.of(existingIngrediente));
        when(ingredienteService.saveIngrediente(existingIngrediente)).thenReturn(existingIngrediente);

        ResponseEntity<Ingrediente> response = ingredienteController.updateIngrediente(1L, updatedIngrediente);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingIngrediente, response.getBody());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(ingredienteService, times(1)).getIngredienteById(1L);
        verify(ingredienteService, times(1)).saveIngrediente(existingIngrediente);
    }

    @Test
    void updateIngrediente_ShouldThrowExceptionWhenNotFound() {
        Ingrediente updatedIngrediente = new Ingrediente();
        updatedIngrediente.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(ingredienteService.getIngredienteById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            ingredienteController.updateIngrediente(1L, updatedIngrediente);
        });

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(ingredienteService, times(1)).getIngredienteById(1L);
        verify(ingredienteService, never()).saveIngrediente(any());
    }

}
