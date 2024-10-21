package com.duoc.recetas.model;

public class Instruccion {

    private int posicion;
    private String descripcion;

    public Instruccion(){}

    public Instruccion(int posicion, String descripcion){
        this.descripcion = descripcion;
        this.posicion = posicion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

   
}
