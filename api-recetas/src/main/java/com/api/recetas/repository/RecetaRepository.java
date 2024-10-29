package com.api.recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.recetas.model.Receta;

public interface RecetaRepository extends JpaRepository<Receta,Long>{

}
