package com.api.recetas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.recetas.model.Receta;
import com.api.recetas.repository.RecetaRepository;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public List<Receta> getAllReceta(){
        return recetaRepository.findAll();
    }

    public Optional<Receta> getRecetaById(Long id){
        return recetaRepository.findById(id);
    }

    public Receta saveReceta(Receta receta){
        return recetaRepository.save(receta);
    }

    public void deleteRecetaById(Long id){
        recetaRepository.deleteById(id);
    }

}
