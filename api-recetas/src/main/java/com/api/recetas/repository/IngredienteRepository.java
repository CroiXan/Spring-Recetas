package com.api.recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.recetas.model.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente,Long>{

}
