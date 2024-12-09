package com.duoc.recetas.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class InstruccionTest {

    @Test
    void testGettersAndSetters() {
        Instruccion instruccion = new Instruccion();

        int posicion = 5;
        String descripcion = "Paso 5: Añadir los ingredientes.";

        instruccion.setPosicion(posicion);
        instruccion.setDescripcion(descripcion);

        assertEquals(posicion, instruccion.getPosicion());
        assertEquals(descripcion, instruccion.getDescripcion());
    }

    @Test
    void testConstructorWithParameters() {
        int posicion = 3;
        String descripcion = "Paso 3: Mezclar bien los ingredientes.";

        Instruccion instruccion = new Instruccion(posicion, descripcion);

        assertEquals(posicion, instruccion.getPosicion());
        assertEquals(descripcion, instruccion.getDescripcion());
    }

    @Test
    void testDefaultConstructor() {
        Instruccion instruccion = new Instruccion();

        assertEquals(0, instruccion.getPosicion());
        assertNull(instruccion.getDescripcion());
    }

}
