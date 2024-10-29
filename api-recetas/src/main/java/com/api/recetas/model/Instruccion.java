package com.api.recetas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Instruccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_instruccion;

    private int posicion;

    private String descripcion;

    public Long getId_instruccion() {
        return id_instruccion;
    }

    public void setId_instruccion(Long id_instruccion) {
        this.id_instruccion = id_instruccion;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
