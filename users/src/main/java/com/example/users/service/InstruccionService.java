package com.example.users.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.users.models.Instruccion;
import com.example.users.repository.InstruccionRepository;

@Service
public class InstruccionService {

    @Autowired
    private InstruccionRepository instruccionRepository;

    public List<Instruccion> getAllInstruccion(){
        return instruccionRepository.findAll();
    }

    public Optional<Instruccion> getInstruccionById(Long id){
        return instruccionRepository.findById(id);
    }

    public Instruccion saveInstruccion(Instruccion order){
        return instruccionRepository.save(order);
    }

    public void deleteInstruccionById(Long id){
        instruccionRepository.deleteById(id);
    }

    public List<Instruccion> getInstruccionByReceta(Long id){
        return instruccionRepository.findById_receta(id);
    }

}
