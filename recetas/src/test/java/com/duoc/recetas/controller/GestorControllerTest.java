package com.duoc.recetas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.recetas.model.IngredienteResponse;
import com.duoc.recetas.model.InstruccionResponse;
import com.duoc.recetas.model.RecetaRequest;
import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.service.MediaFileService;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GestorControllerTest {

    @InjectMocks
    private GestorController gestorController;

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
    void testGestor() {
        List<RecetaResponse> mockRecetas = new ArrayList<>();
        RecetaResponse receta1 = new RecetaResponse();
        receta1.setNombre("Receta 1");
        mockRecetas.add(receta1);

        when(recetaService.getAllRecetaService()).thenReturn(Flux.fromIterable(mockRecetas));

        String viewName = gestorController.gestor(model);

        assertEquals("gestor", viewName);
        verify(model, times(1)).addAttribute("listarecetas", mockRecetas);
        verify(recetaService, times(1)).getAllRecetaService();
    }

    @Test
    void testAgregarReceta() {
        String nombreReceta = "Nueva Receta";
        RecetaRequest mockRequest = new RecetaRequest();
        mockRequest.setNombre(nombreReceta);

        when(recetaService.addReceta(any())).thenReturn(Mono.empty());

        String viewName = gestorController.agregarReceta(nombreReceta);

        assertEquals("redirect:/gestor", viewName);
        verify(recetaService, times(1)).addReceta(any(RecetaRequest.class));
    }

    @Test
    void testGuardarArchivo() throws Exception {
        Long recetaId = 1L;
        String nombre = "Archivo Receta";
        String descripcion = "Descripción del archivo";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("mockfile.txt");
        doNothing().when(mockFile).transferTo(any(File.class));
        when(mediaFileService.uploadFile(any(), anyString(), anyLong())).thenReturn(Mono.just("Archivo subido"));

        String viewName = gestorController.guardarArchivo(recetaId, nombre, descripcion, mockFile, model);

        assertEquals("redirect:/editar/" + recetaId, viewName);
        verify(mockFile, times(1)).transferTo(any(File.class));
        verify(mediaFileService, times(1)).uploadFile(any(), anyString(), eq(recetaId));
        verify(model, times(1)).addAttribute("message", "Receta actualizada correctamente.");
    }

    @Test
    void testAddIngrediente() {
        Long recetaId = 1L;
        String descripcion = "Nuevo Ingrediente";
        IngredienteResponse mockIngrediente = new IngredienteResponse();
        mockIngrediente.setNombr_item(descripcion);

        RecetaResponse mockSelectedReceta = new RecetaResponse();
        mockSelectedReceta.setListaIngredientes(new ArrayList<>());
        gestorController.selectedReceta = mockSelectedReceta;

        when(recetaService.addIngrediente(any())).thenReturn(Mono.just(mockIngrediente));

        String viewName = gestorController.addIngrediente(recetaId, descripcion);

        assertEquals("redirect:/editar/" + recetaId, viewName);
        verify(recetaService, times(1)).addIngrediente(any());
    }

    @Test
    void testEditIngrediente_editar() {
        Long recetaId = 1L;
        Long ingredienteId = 2L;
        String descripcion = "Ingrediente Editado";
        String action = "editar";

        IngredienteResponse mockIngrediente = new IngredienteResponse();
        mockIngrediente.setId_ingrediente(ingredienteId);

        when(recetaService.getIngredienteById(ingredienteId)).thenReturn(Mono.just(mockIngrediente));
        doNothing().when(recetaService).editarIngrediente(mockIngrediente);

        String viewName = gestorController.editIngrediente(recetaId, ingredienteId, descripcion, action);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(1)).getIngredienteById(ingredienteId);
        verify(recetaService, times(1)).editarIngrediente(mockIngrediente);
        verify(recetaService, times(0)).eliminarIngrediente(anyLong());
        assertEquals(descripcion, mockIngrediente.getNombr_item());
    }

    @Test
    void testEditIngrediente_eliminar() {
        Long recetaId = 1L;
        Long ingredienteId = 2L;
        String descripcion = "";
        String action = "eliminar";

        doNothing().when(recetaService).eliminarIngrediente(ingredienteId);

        String viewName = gestorController.editIngrediente(recetaId, ingredienteId, descripcion, action);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(0)).getIngredienteById(anyLong());
        verify(recetaService, times(0)).editarIngrediente(any());
        verify(recetaService, times(1)).eliminarIngrediente(ingredienteId);
    }

    @Test
    void testEditarInstruccion_editar() {
        Long recetaId = 1L;
        Long instruccionId = 2L;
        String descripcion = "Instrucción Editada";
        Integer posicion = 3;
        String action = "editar";

        InstruccionResponse mockInstruccion = new InstruccionResponse();
        mockInstruccion.setId_instruccion(instruccionId);

        when(recetaService.getInstruccionById(instruccionId)).thenReturn(Mono.just(mockInstruccion));
        doNothing().when(recetaService).editarInstruccion(mockInstruccion);

        String viewName = gestorController.editarInstruccion(recetaId, instruccionId, descripcion, posicion, action);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(1)).getInstruccionById(instruccionId);
        verify(recetaService, times(1)).editarInstruccion(mockInstruccion);
        verify(recetaService, times(0)).eliminarInstruccion(anyLong());
        assertEquals(descripcion, mockInstruccion.getDescripcion());
        assertEquals(posicion, mockInstruccion.getPosicion());
    }

    @Test
    void testEditarInstruccion_eliminar() {
        Long recetaId = 1L;
        Long instruccionId = 2L;
        String descripcion = "";
        Integer posicion = 0;
        String action = "eliminar";

        doNothing().when(recetaService).eliminarInstruccion(instruccionId);

        String viewName = gestorController.editarInstruccion(recetaId, instruccionId, descripcion, posicion, action);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(0)).getInstruccionById(anyLong());
        verify(recetaService, times(0)).editarInstruccion(any());
        verify(recetaService, times(1)).eliminarInstruccion(instruccionId);
    }

    @Test
    void testEditar() {
        Long recetaId = 1L;

        RecetaResponse mockReceta = new RecetaResponse();
        mockReceta.setId_receta(recetaId);
        mockReceta.setNombre("Receta Test");
        mockReceta.setListaIngredientes(new ArrayList<>());
        mockReceta.setListaInstrucciones(new ArrayList<>());

        when(recetaService.getRecetaById(recetaId)).thenReturn(Mono.just(mockReceta));

        String viewName = gestorController.editar(recetaId, model);

        assertEquals("editarreceta", viewName);

        verify(recetaService, times(1)).getRecetaById(recetaId);
        verify(model, times(1)).addAttribute("receta", mockReceta);
        verify(model, times(1)).addAttribute("nombre", "");
        verify(model, times(1)).addAttribute("descripcion", "");

        assertNotNull(gestorController.selectedReceta);
        assertEquals("Receta Test", gestorController.selectedReceta.getNombre());
    }

    @Test
    void testGuardarEditar() {
        Long recetaId = 1L;
        String nombreReceta = "Nueva Receta Editada";

        RecetaResponse mockReceta = new RecetaResponse();
        mockReceta.setId_receta(recetaId);
        mockReceta.setNombre("Receta Original");

        when(recetaService.getRecetaById(recetaId)).thenReturn(Mono.just(mockReceta));
        doNothing().when(recetaService).editarReceta(mockReceta);

        String viewName = gestorController.guardarEditar(recetaId, nombreReceta);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(1)).getRecetaById(recetaId);
        verify(recetaService, times(1)).editarReceta(mockReceta);

        assertEquals(nombreReceta, mockReceta.getNombre());
    }

    @Test
    void testAgregarInstruccion() {
        Long recetaId = 1L;
        String descripcion = "Nueva Instrucción";
        Integer posicion = 2;

        doNothing().when(recetaService).agregarInstruccion(eq(recetaId), eq(descripcion), eq(posicion));

        String viewName = gestorController.agregarInstruccion(recetaId, descripcion, posicion);

        assertEquals("redirect:/editar/" + recetaId, viewName);

        verify(recetaService, times(1)).agregarInstruccion(recetaId, descripcion, posicion);
    }
}
