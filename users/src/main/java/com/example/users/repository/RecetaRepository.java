package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.users.models.Receta;
import java.util.List;


public interface RecetaRepository extends JpaRepository<Receta,Long>{

    @Query("SELECT r FROM Receta r WHERE r.nombre LIKE %:nombre%")
    List<Receta> findByLikeNombre(@Param("nombre") String nombre);
}
