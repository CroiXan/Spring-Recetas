package com.example.users.controller;

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

import com.example.users.exception.ResourceNotFoundException;
import com.example.users.models.Ingrediente;
import com.example.users.service.IngredienteService;
import com.example.users.service.RecetaService;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<Ingrediente>> getAllIngredientes() {
        List<Ingrediente> ingrediente = ingredienteService.getAllIngrediente();
        return ResponseEntity.ok(ingrediente);
    }
    
    @PostMapping
    public ResponseEntity<Ingrediente> createIngrediente(@RequestBody Ingrediente newIngrediente) {
        recetaService.getRecetaById(newIngrediente.getId_receta())
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ newIngrediente.getId_receta() +" no se encuentra"));
        Ingrediente ingrediente = ingredienteService.saveIngrediente(newIngrediente);
        return new ResponseEntity<>(ingrediente, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> getIngrediente(@PathVariable Long id) {
        Ingrediente ingrediente = ingredienteService.getIngredienteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ingrediente con ID "+ id +" no se encuentra"));
        return ResponseEntity.ok(ingrediente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngrediente(@PathVariable Long id){
        ingredienteService.getIngredienteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ingrediente con ID "+ id +" no se encuentra"));
        ingredienteService.deleteIngredienteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> updateIngrediente(@PathVariable Long id, @RequestBody Ingrediente updatedIngrediente ) {
        recetaService.getRecetaById(updatedIngrediente.getId_receta())
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ updatedIngrediente.getId_receta() +" no se encuentra"));
        Ingrediente ingrediente = ingredienteService.getIngredienteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ingrediente con ID "+ id +" no se encuentra"));
        ingrediente.setId_receta(updatedIngrediente.getId_receta());
        ingrediente.setNombr_item(updatedIngrediente.getNombr_item());
        Ingrediente resultIngrediente = ingredienteService.saveIngrediente(ingrediente);
        return ResponseEntity.ok(resultIngrediente);
    }

}
