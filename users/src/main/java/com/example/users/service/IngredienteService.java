package com.example.users.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.users.models.Ingrediente;
import com.example.users.repository.IngredienteRepository;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<Ingrediente> getAllIngrediente(){
        return ingredienteRepository.findAll();
    }

    public Optional<Ingrediente> getIngredienteById(Long id){
        return ingredienteRepository.findById(id);
    }

    public Ingrediente saveIngrediente(Ingrediente ingrediente){
        return ingredienteRepository.save(ingrediente);
    }

    public void deleteIngredienteById(Long id){
        ingredienteRepository.deleteById(id);
    }

    public List<Ingrediente> getIngredientesByReceta(Long id){
        return ingredienteRepository.findById_receta(id);
    }

}
