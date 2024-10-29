package com.api.recetas.controller;

import java.util.List;
import java.util.Optional;

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

import com.api.recetas.model.Receta;
import com.api.recetas.service.RecetaService;

@RestController
@RequestMapping("/api/receta")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<Receta>> getAllReceta() {
        List<Receta> receta = recetaService.getAllReceta();
        return ResponseEntity.ok(receta);
    }
    
    @PostMapping
    public ResponseEntity<Receta> createReceta(@RequestBody Receta receta) {
        Receta newReceta = recetaService.saveReceta(receta);
        return new ResponseEntity<>(newReceta, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Receta>> getReceta(@PathVariable Long id) {
        Optional<Receta> receta = recetaService.getRecetaById(id);
        return ResponseEntity.ok(receta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceta(@PathVariable Long id){
        recetaService.deleteRecetaById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receta> updateReceta(@PathVariable Long id, @RequestBody Receta updatedReceta ) {
        Optional<Receta> receta = recetaService.getRecetaById(id);
        if(receta.isPresent()){
            Receta localReceta = receta.get();
            localReceta.setNombre(updatedReceta.getNombre());
            Receta resulReceta = recetaService.saveReceta(localReceta);
            return ResponseEntity.ok(resulReceta);
        }
        return ResponseEntity.noContent().build();
    }
}
