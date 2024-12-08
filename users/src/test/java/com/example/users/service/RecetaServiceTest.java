package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.example.users.models.FullReceta;
import com.example.users.models.Ingrediente;
import com.example.users.models.Instruccion;
import com.example.users.models.Receta;
import com.example.users.repository.IngredienteRepository;
import com.example.users.repository.InstruccionRepository;
import com.example.users.repository.RecetaRepository;

public class RecetaServiceTest {

    @InjectMocks
    private RecetaService recetaService;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private InstruccionRepository instruccionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReceta_ShouldReturnListOfFullRecetas() {
        Receta receta1 = new Receta();
        receta1.setId_receta(1L);
        receta1.setNombre("Receta 1");

        Receta receta2 = new Receta();
        receta2.setId_receta(2L);
        receta2.setNombre("Receta 2");

        when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta1, receta2));
        when(ingredienteRepository.findAll()).thenReturn(Arrays.asList());
        when(instruccionRepository.findAll()).thenReturn(Arrays.asList());

        List<FullReceta> result = recetaService.getAllReceta();

        assertEquals(2, result.size());
        assertEquals("Receta 1", result.get(0).getNombre());
        assertEquals("Receta 2", result.get(1).getNombre());
    }

    @Test
    void getRecetaById_ShouldReturnReceta() {
        Receta receta = new Receta();
        receta.setId_receta(1L);

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        Optional<Receta> result = recetaService.getRecetaById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId_receta());
    }

    @Test
    void saveReceta_ShouldSaveAndReturnReceta() {
        Receta receta = new Receta();
        receta.setNombre("New Receta");

        when(recetaRepository.save(receta)).thenReturn(receta);

        Receta result = recetaService.saveReceta(receta);

        assertNotNull(result);
        assertEquals("New Receta", result.getNombre());
    }

    @Test
    void deleteRecetaById_ShouldDeleteReceta() {
        Long recetaId = 1L;

        recetaService.deleteRecetaById(recetaId);

        verify(recetaRepository, times(1)).deleteById(recetaId);
    }

    @Test
    void findRecetaByName_ShouldReturnMatchingRecetas() {
        Receta receta = new Receta();
        receta.setId_receta(1L);
        receta.setNombre("Test Receta");

        when(recetaRepository.findByLikeNombre("Test")).thenReturn(Arrays.asList(receta));
        when(ingredienteRepository.findAll()).thenReturn(Arrays.asList());
        when(instruccionRepository.findAll()).thenReturn(Arrays.asList());

        List<FullReceta> result = recetaService.findRecetaByName("Test");

        assertEquals(1, result.size());
        assertEquals("Test Receta", result.get(0).getNombre());
    }

    @Test
    void getFullRecetabyId_ShouldReturnFullReceta() {
        Receta receta = new Receta();
        receta.setId_receta(1L);
        receta.setNombre("Receta Completa");

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId_receta(1L);

        Instruccion instruccion = new Instruccion();
        instruccion.setId_receta(1L);
        instruccion.setPosicion(1);

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        when(ingredienteRepository.findById_receta(1L)).thenReturn(Arrays.asList(ingrediente));
        when(instruccionRepository.findById_receta(1L)).thenReturn(Arrays.asList(instruccion));

        Optional<FullReceta> result = recetaService.getFullRecetabyId(1L);

        assertTrue(result.isPresent());
        assertEquals("Receta Completa", result.get().getNombre());
        assertEquals(1, result.get().getListaIngredientes().size());
        assertEquals(1, result.get().getListaInstrucciones().size());
    }

    @Test
    void getFullRecetabyId_ShouldReturnEmptyWhenNotFound() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<FullReceta> result = recetaService.getFullRecetabyId(1L);

        assertFalse(result.isPresent());
    }
    
}
