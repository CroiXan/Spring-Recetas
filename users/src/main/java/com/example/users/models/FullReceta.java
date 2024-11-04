package com.example.users.models;

import java.util.List;

public class FullReceta extends Receta{

    private List<Instruccion> listaInstrucciones;
    private List<Ingrediente> listaIngredientes;
    
    public List<Instruccion> getListaInstrucciones() {
        return listaInstrucciones;
    }
    public void setListaInstrucciones(List<Instruccion> listaInstrucciones) {
        this.listaInstrucciones = listaInstrucciones;
    }
    public List<Ingrediente> getListaIngredientes() {
        return listaIngredientes;
    }
    public void setListaIngredientes(List<Ingrediente> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }
}
