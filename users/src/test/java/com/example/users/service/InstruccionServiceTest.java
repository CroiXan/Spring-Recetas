package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.users.models.Instruccion;
import com.example.users.repository.InstruccionRepository;

public class InstruccionServiceTest {
    @InjectMocks
    private InstruccionService instruccionService;

    @Mock
    private InstruccionRepository instruccionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllInstruccion() {
        List<Instruccion> mockInstrucciones = new ArrayList<>();
        Instruccion instruccion1 = new Instruccion();
        Instruccion instruccion2 = new Instruccion();
        instruccion1.setId_instruccion(1L);
        instruccion1.setDescripcion("Paso 1");
        instruccion1.setId_receta(1L);
        instruccion2.setId_instruccion(2L);
        instruccion2.setDescripcion("Paso 2");
        instruccion2.setId_receta(1L);
        mockInstrucciones.add(instruccion1);
        mockInstrucciones.add(instruccion2);

        when(instruccionRepository.findAll()).thenReturn(mockInstrucciones);

        List<Instruccion> result = instruccionService.getAllInstruccion();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(instruccionRepository, times(1)).findAll();
    }

    @Test
    void testGetInstruccionById_Found() {
        Instruccion mockInstruccion = new Instruccion();
        mockInstruccion.setId_instruccion(1L);
        mockInstruccion.setDescripcion("Paso 1");
        mockInstruccion.setId_receta(1L);
        when(instruccionRepository.findById(1L)).thenReturn(Optional.of(mockInstruccion));

        Optional<Instruccion> result = instruccionService.getInstruccionById(1L);

        assertTrue(result.isPresent());
        assertEquals("Paso 1", result.get().getDescripcion());
        verify(instruccionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetInstruccionById_NotFound() {
        when(instruccionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Instruccion> result = instruccionService.getInstruccionById(1L);

        assertFalse(result.isPresent());
        verify(instruccionRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveInstruccion() {
        Instruccion mockInstruccion = new Instruccion();
        mockInstruccion.setId_instruccion(1L);
        mockInstruccion.setDescripcion("Paso 1");
        mockInstruccion.setId_receta(1L);
        when(instruccionRepository.save(mockInstruccion)).thenReturn(mockInstruccion);

        Instruccion result = instruccionService.saveInstruccion(mockInstruccion);

        assertNotNull(result);
        assertEquals("Paso 1", result.getDescripcion());
        verify(instruccionRepository, times(1)).save(mockInstruccion);
    }

    @Test
    void testDeleteInstruccionById() {
        doNothing().when(instruccionRepository).deleteById(1L);

        instruccionService.deleteInstruccionById(1L);

        verify(instruccionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetInstruccionByReceta() {
        List<Instruccion> mockInstrucciones = new ArrayList<>();
        Instruccion instruccion1 = new Instruccion();
        Instruccion instruccion2 = new Instruccion();
        instruccion1.setId_instruccion(1L);
        instruccion1.setDescripcion("Paso 1");
        instruccion1.setId_receta(1L);
        instruccion2.setId_instruccion(2L);
        instruccion2.setDescripcion("Paso 2");
        instruccion2.setId_receta(1L);
        mockInstrucciones.add(instruccion1);
        mockInstrucciones.add(instruccion2);

        when(instruccionRepository.findById_receta(1L)).thenReturn(mockInstrucciones);

        List<Instruccion> result = instruccionService.getInstruccionByReceta(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(instruccionRepository, times(1)).findById_receta(1L);
    }
}
