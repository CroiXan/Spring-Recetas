package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.users.models.Instruccion;
import java.util.List;


public interface InstruccionRepository extends JpaRepository<Instruccion,Long>{

    @Query("SELECT i FROM Instruccion i WHERE i.id_receta = :id_receta")
    List<Instruccion> findById_receta(Long id_receta);

}
