package com.api.recetas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.recetas.exception.ResourceNotFoundException;
import com.api.recetas.model.FullReceta;
import com.api.recetas.model.Receta;
import com.api.recetas.service.RecetaService;

@RestController
@RequestMapping("/api/receta")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<FullReceta>> getAllReceta() {
        List<FullReceta> receta = recetaService.getAllReceta();
        return ResponseEntity.ok(receta);
    }
    
    @PostMapping
    public ResponseEntity<Receta> createReceta(@RequestBody Receta receta) {
        Receta newReceta = recetaService.saveReceta(receta);
        return new ResponseEntity<>(newReceta, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FullReceta> getReceta(@PathVariable Long id) {
        FullReceta receta = recetaService.getFullRecetabyId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ id +" no se encuentra"));
        return ResponseEntity.ok(receta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceta(@PathVariable Long id){
        recetaService.getRecetaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ id +" no se encuentra"));
        recetaService.deleteRecetaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receta> updateReceta(@PathVariable Long id, @RequestBody Receta updatedReceta ) {
        Receta receta = recetaService.getRecetaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ id +" no se encuentra"));
        receta.setNombre(updatedReceta.getNombre());
        Receta resulReceta = recetaService.saveReceta(receta);
        return ResponseEntity.ok(resulReceta);
    }
}
