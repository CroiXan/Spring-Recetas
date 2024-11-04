package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.users.models.Ingrediente;
import java.util.List;


public interface IngredienteRepository extends JpaRepository<Ingrediente,Long>{

    @Query("SELECT i FROM Ingrediente i WHERE i.id_receta = :id_receta")
    List<Ingrediente> findById_receta(Long id_receta);

}
