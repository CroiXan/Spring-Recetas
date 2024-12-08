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
import com.example.users.models.Instruccion;
import com.example.users.models.Receta;
import com.example.users.service.InstruccionService;
import com.example.users.service.RecetaService;

public class InstruccionControllerTest {

    @InjectMocks
    private InstruccionController instruccionController;

    @Mock
    private InstruccionService instruccionService;

    @Mock
    private RecetaService recetaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllInstrucciones_ShouldReturnListOfInstrucciones() {
        List<Instruccion> instrucciones = Arrays.asList(new Instruccion(), new Instruccion());
        when(instruccionService.getAllInstruccion()).thenReturn(instrucciones);

        ResponseEntity<List<Instruccion>> response = instruccionController.getAllInstrucciones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instrucciones, response.getBody());
        verify(instruccionService, times(1)).getAllInstruccion();
    }

    @Test
    void createInstruccion_ShouldReturnCreatedInstruccion() {
        Instruccion newInstruccion = new Instruccion();
        newInstruccion.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(instruccionService.saveInstruccion(newInstruccion)).thenReturn(newInstruccion);

        ResponseEntity<Instruccion> response = instruccionController.createInstruccion(newInstruccion);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newInstruccion, response.getBody());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(instruccionService, times(1)).saveInstruccion(newInstruccion);
    }

    @Test
    void createInstruccion_ShouldThrowExceptionWhenRecetaNotFound() {
        Instruccion newInstruccion = new Instruccion();
        newInstruccion.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instruccionController.createInstruccion(newInstruccion);
        });

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(instruccionService, never()).saveInstruccion(any());
    }

    @Test
    void getInstruccion_ShouldReturnInstruccion() {
        Instruccion instruccion = new Instruccion();
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.of(instruccion));

        ResponseEntity<Instruccion> response = instruccionController.getInstruccion(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(instruccion, response.getBody());
        verify(instruccionService, times(1)).getInstruccionById(1L);
    }

    @Test
    void getInstruccion_ShouldThrowExceptionWhenNotFound() {
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instruccionController.getInstruccion(1L);
        });

        verify(instruccionService, times(1)).getInstruccionById(1L);
    }

    @Test
    void deleteInstruccion_ShouldReturnNoContent() {
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.of(new Instruccion()));

        ResponseEntity<Void> response = instruccionController.deleteInstruccion(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(instruccionService, times(1)).getInstruccionById(1L);
        verify(instruccionService, times(1)).deleteInstruccionById(1L);
    }

    @Test
    void deleteInstruccion_ShouldThrowExceptionWhenNotFound() {
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instruccionController.deleteInstruccion(1L);
        });

        verify(instruccionService, times(1)).getInstruccionById(1L);
        verify(instruccionService, never()).deleteInstruccionById(any());
    }

    @Test
    void updateInstruccion_ShouldReturnUpdatedInstruccion() {
        Instruccion existingInstruccion = new Instruccion();
        Instruccion updatedInstruccion = new Instruccion();
        updatedInstruccion.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.of(existingInstruccion));
        when(instruccionService.saveInstruccion(existingInstruccion)).thenReturn(existingInstruccion);

        ResponseEntity<Instruccion> response = instruccionController.updateInstruccion(1L, updatedInstruccion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingInstruccion, response.getBody());
        verify(recetaService, times(1)).getRecetaById(1L);
        verify(instruccionService, times(1)).getInstruccionById(1L);
        verify(instruccionService, times(1)).saveInstruccion(existingInstruccion);
    }

    @Test
    void updateInstruccion_ShouldThrowExceptionWhenNotFound() {
        Instruccion updatedInstruccion = new Instruccion();
        updatedInstruccion.setId_receta(1L);
        when(recetaService.getRecetaById(1L)).thenReturn(Optional.of(new Receta()));
        when(instruccionService.getInstruccionById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            instruccionController.updateInstruccion(1L, updatedInstruccion);
        });

        verify(recetaService, times(1)).getRecetaById(1L);
        verify(instruccionService, times(1)).getInstruccionById(1L);
        verify(instruccionService, never()).saveInstruccion(any());
    }

}
