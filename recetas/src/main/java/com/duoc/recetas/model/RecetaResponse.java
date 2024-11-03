package com.duoc.recetas.model;

import java.util.List;

public class RecetaResponse {

    private Long id_receta;
    
    private String nombre;

    private List<InstruccionResponse> listaInstrucciones;

    private List<IngredienteResponse> listaIngredientes;

    public Long getId_receta() {
        return id_receta;
    }

    public void setId_receta(Long id_receta) {
        this.id_receta = id_receta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<InstruccionResponse> getListaInstrucciones() {
        return listaInstrucciones;
    }

    public void setListaInstrucciones(List<InstruccionResponse> listaInstrucciones) {
        this.listaInstrucciones = listaInstrucciones;
    }

    public List<IngredienteResponse> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(List<IngredienteResponse> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }
    
}
