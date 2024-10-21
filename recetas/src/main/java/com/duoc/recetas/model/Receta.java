package com.duoc.recetas.model;

import java.util.List;

public class Receta {

    private Long id;
    private String nombre;
    private List<String> ingredientes;
    private List<Instruccion> instrucciones;
    private String foto;

    public Receta(){}

    public Receta(Long id, String nombre, List<String> ingredientes, List<Instruccion> instrucciones, String foto){
        this.id = id;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<String> getIngredientes() {
        return ingredientes;
    }
    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }
    public List<Instruccion> getInstrucciones() {
        return instrucciones;
    }
    public void setInstrucciones(List<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
    }
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void addIngrediente(String ingrediente){
        ingredientes.add(ingrediente);
    }

    public void addInstruccion(Instruccion instruccion){
        instrucciones.add(instruccion);
    }
    
}
