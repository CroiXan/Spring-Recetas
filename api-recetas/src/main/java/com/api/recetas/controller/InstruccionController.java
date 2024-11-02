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
import com.api.recetas.model.Instruccion;
import com.api.recetas.service.InstruccionService;
import com.api.recetas.service.RecetaService;

@RestController
@RequestMapping("/api/instrucciones")
public class InstruccionController {

    @Autowired
    private InstruccionService instruccionService;

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<Instruccion>> getAllInstrucciones() {
        List<Instruccion> instruccion = instruccionService.getAllInstruccion();
        return ResponseEntity.ok(instruccion);
    }
    
    @PostMapping
    public ResponseEntity<Instruccion> createInstruccion(@RequestBody Instruccion newInstruccion) {
        recetaService.getRecetaById(newInstruccion.getId_receta())
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ newInstruccion.getId_receta() +" no se encuentra"));
        Instruccion instruccion = instruccionService.saveInstruccion(newInstruccion);
        return new ResponseEntity<>(instruccion, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Instruccion> getInstruccion(@PathVariable Long id) {
        Instruccion instruccion = instruccionService.getInstruccionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instruccion con ID "+ id +" no se encuentra"));
        return ResponseEntity.ok(instruccion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstruccion(@PathVariable Long id){
        instruccionService.getInstruccionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instruccion con ID "+ id +" no se encuentra"));
        instruccionService.getInstruccionById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instruccion> updateInstruccion(@PathVariable Long id, @RequestBody Instruccion updatedInstruccion ) {
        recetaService.getRecetaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Receta con ID "+ id +" no se encuentra"));
        Instruccion instruccion = instruccionService.getInstruccionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instruccion con ID "+ id +" no se encuentra"));
        instruccion.setId_receta(updatedInstruccion.getId_receta());
        instruccion.setDescripcion(updatedInstruccion.getDescripcion());
        instruccion.setPosicion(updatedInstruccion.getPosicion());
        Instruccion resultInstruccion = instruccionService.saveInstruccion(instruccion);
        return ResponseEntity.ok(resultInstruccion);
    }

}
