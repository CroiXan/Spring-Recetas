package com.api.recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.recetas.model.Instruccion;

public interface InstruccionRepository extends JpaRepository<Instruccion,Long>{

}
