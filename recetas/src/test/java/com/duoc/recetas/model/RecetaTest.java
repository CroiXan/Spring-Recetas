package com.duoc.recetas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RecetaTest {
    @Test
    void testGettersAndSetters() {
        Receta receta = new Receta();

        Long id = 1L;
        String nombre = "Receta de prueba";
        List<String> ingredientes = new ArrayList<>();
        ingredientes.add("Ingrediente 1");
        ingredientes.add("Ingrediente 2");
        List<Instruccion> instrucciones = new ArrayList<>();
        instrucciones.add(new Instruccion(1, "Paso 1"));
        instrucciones.add(new Instruccion(2, "Paso 2"));
        String foto = "imagen.jpg";

        receta.setId(id);
        receta.setNombre(nombre);
        receta.setIngredientes(ingredientes);
        receta.setInstrucciones(instrucciones);
        receta.setFoto(foto);

        assertEquals(id, receta.getId());
        assertEquals(nombre, receta.getNombre());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals(foto, receta.getFoto());
    }

    @Test
    void testConstructorWithParameters() {
        Long id = 2L;
        String nombre = "Otra Receta";
        List<String> ingredientes = List.of("Ingrediente A", "Ingrediente B");
        List<Instruccion> instrucciones = List.of(
                new Instruccion(1, "Paso A"),
                new Instruccion(2, "Paso B"));
        String foto = "otraImagen.jpg";

        Receta receta = new Receta(id, nombre, ingredientes, instrucciones, foto);

        assertEquals(id, receta.getId());
        assertEquals(nombre, receta.getNombre());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals(foto, receta.getFoto());
    }

    @Test
    void testDefaultConstructor() {
        Receta receta = new Receta();

        assertNull(receta.getId());
        assertNull(receta.getNombre());
        assertNull(receta.getIngredientes());
        assertNull(receta.getInstrucciones());
        assertNull(receta.getFoto());
    }

    @Test
    void testAddIngrediente() {
        Receta receta = new Receta();
        receta.setIngredientes(new ArrayList<>());

        receta.addIngrediente("Ingrediente 1");
        receta.addIngrediente("Ingrediente 2");

        List<String> expected = List.of("Ingrediente 1", "Ingrediente 2");
        assertEquals(expected, receta.getIngredientes());
    }

    @Test
    void testAddInstruccion() {
        Receta receta = new Receta();
        receta.setInstrucciones(new ArrayList<>());

        receta.addInstruccion(new Instruccion(1, "Paso 1"));
        receta.addInstruccion(new Instruccion(2, "Paso 2"));

        List<Instruccion> expected = List.of(
                new Instruccion(1, "Paso 1"),
                new Instruccion(2, "Paso 2"));
        assertEquals(expected.size(), receta.getInstrucciones().size());
        assertEquals(expected.get(0).getDescripcion(), receta.getInstrucciones().get(0).getDescripcion());
        assertEquals(expected.get(1).getDescripcion(), receta.getInstrucciones().get(1).getDescripcion());
    }
}
