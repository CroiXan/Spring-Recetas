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

import com.example.users.models.Ingrediente;
import com.example.users.repository.IngredienteRepository;

public class IngredienteServiceTest {

    @InjectMocks
    private IngredienteService ingredienteService;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllIngrediente() {
        List<Ingrediente> mockIngredientes = new ArrayList<>();
        Ingrediente ingrediente1 = new Ingrediente();
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente1.setId_ingrediente(1L);
        ingrediente1.setId_receta(1L);
        ingrediente1.setNombr_item("Harina 500g");
        ingrediente2.setId_ingrediente(2L);
        ingrediente2.setId_receta(1L);
        ingrediente2.setNombr_item("Azúcar 200g");
        mockIngredientes.add(ingrediente1);
        mockIngredientes.add(ingrediente2);

        when(ingredienteRepository.findAll()).thenReturn(mockIngredientes);

        List<Ingrediente> result = ingredienteService.getAllIngrediente();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ingredienteRepository, times(1)).findAll();
    }

    @Test
    void testGetIngredienteById_Found() {
        Ingrediente mockIngrediente = new Ingrediente();
        mockIngrediente.setId_ingrediente(1L);
        mockIngrediente.setId_receta(1L);
        mockIngrediente.setNombr_item("Harina 500g");

        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(mockIngrediente));

        Optional<Ingrediente> result = ingredienteService.getIngredienteById(1L);

        assertTrue(result.isPresent());
        assertEquals("Harina 500g", result.get().getNombr_item());
        verify(ingredienteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetIngredienteById_NotFound() {
        when(ingredienteRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Ingrediente> result = ingredienteService.getIngredienteById(1L);

        assertFalse(result.isPresent());
        verify(ingredienteRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveIngrediente() {
        Ingrediente mockIngrediente = new Ingrediente();
        mockIngrediente.setId_ingrediente(1L);
        mockIngrediente.setId_receta(1L);
        mockIngrediente.setNombr_item("Harina 500g");

        when(ingredienteRepository.save(mockIngrediente)).thenReturn(mockIngrediente);

        Ingrediente result = ingredienteService.saveIngrediente(mockIngrediente);

        assertNotNull(result);
        assertEquals("Harina 500g", result.getNombr_item());
        verify(ingredienteRepository, times(1)).save(mockIngrediente);
    }

    @Test
    void testDeleteIngredienteById() {
        doNothing().when(ingredienteRepository).deleteById(1L);

        ingredienteService.deleteIngredienteById(1L);

        verify(ingredienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetIngredientesByReceta() {
        List<Ingrediente> mockIngredientes = new ArrayList<>();
        Ingrediente ingrediente1 = new Ingrediente();
        Ingrediente ingrediente2 = new Ingrediente();
        ingrediente1.setId_ingrediente(1L);
        ingrediente1.setId_receta(1L);
        ingrediente1.setNombr_item("Harina 500g");
        ingrediente2.setId_ingrediente(2L);
        ingrediente2.setId_receta(1L);
        ingrediente2.setNombr_item("Azúcar 200g");
        mockIngredientes.add(ingrediente1);
        mockIngredientes.add(ingrediente2);

        when(ingredienteRepository.findById_receta(1L)).thenReturn(mockIngredientes);

        List<Ingrediente> result = ingredienteService.getIngredientesByReceta(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ingredienteRepository, times(1)).findById_receta(1L);
    }
}
