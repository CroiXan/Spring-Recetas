package com.api.recetas.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.recetas.model.FullReceta;
import com.api.recetas.model.Ingrediente;
import com.api.recetas.model.Instruccion;
import com.api.recetas.model.Receta;
import com.api.recetas.repository.IngredienteRepository;
import com.api.recetas.repository.InstruccionRepository;
import com.api.recetas.repository.RecetaRepository;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private InstruccionRepository instruccionRepository;

    public List<FullReceta> getAllReceta(){
        return this.buildFullRecetasList();
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

    public Optional<FullReceta> getFullRecetabyId(Long id){
        Optional<Receta> receta = recetaRepository.findById(id);

        if (receta.isPresent()) {
            FullReceta localFullReceta = new FullReceta();
            
            localFullReceta.setId_receta(receta.get().getId_receta());
            localFullReceta.setNombre(receta.get().getNombre());
            localFullReceta.setListaIngredientes(ingredienteRepository.findById_receta(receta.get().getId_receta()));
            localFullReceta.setListaInstrucciones(instruccionRepository.findById_receta(receta.get().getId_receta()));

            Optional<FullReceta> fullReceta = Optional.of(localFullReceta); 

            return fullReceta;
        }

        return Optional.empty();
    }

    private List<FullReceta> buildFullRecetasList(){
        List<FullReceta> listaRecetas = new ArrayList<FullReceta>();
        List<Receta> recetas = recetaRepository.findAll();
        List<Ingrediente> ingredientes = ingredienteRepository.findAll();
        List<Instruccion> instrucciones = instruccionRepository.findAll();

        for (Receta receta : recetas) {
            FullReceta localFullReceta = new FullReceta();

            List<Ingrediente> localIngredientes = ingredientes.stream()
                .filter(ingrediente -> ingrediente.getId_receta() == receta.getId_receta())
                .collect(Collectors.toList());
            
            List<Instruccion> localInstrucciones = instrucciones.stream()
                .filter(instruccion -> instruccion.getId_receta() == receta.getId_receta())
                .sorted(Comparator.comparingLong(Instruccion::getPosicion))
                .collect(Collectors.toList());

            localFullReceta.setId_receta(receta.getId_receta());
            localFullReceta.setNombre(receta.getNombre());
            localFullReceta.setListaIngredientes(localIngredientes);
            localFullReceta.setListaInstrucciones(localInstrucciones);

            listaRecetas.add(localFullReceta);
        }

        return listaRecetas;
    }

}
