package com.example.users.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ingrediente;

    private Long id_receta;

    private String nombr_item;

    public Long getId_ingrediente() {
        return id_ingrediente;
    }

    public void setId_ingrediente(Long id_ingrediente) {
        this.id_ingrediente = id_ingrediente;
    }

    public Long getId_receta() {
        return id_receta;
    }

    public void setId_receta(Long id_receta) {
        this.id_receta = id_receta;
    }

    public String getNombr_item() {
        return nombr_item;
    }

    public void setNombr_item(String nombr_item) {
        this.nombr_item = nombr_item;
    }
}
