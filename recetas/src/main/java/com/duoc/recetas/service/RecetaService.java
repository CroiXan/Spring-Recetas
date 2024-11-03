package com.duoc.recetas.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Collectors;
import com.duoc.recetas.model.Instruccion;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaResponse;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecetaService {
    private List<Receta> recetas = new ArrayList<>();
    private Long nextId = 1L;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private WebClient webClient;

    public RecetaService(){
        List<String> ingredientes1 = new ArrayList<>();
        ingredientes1.add("1 kg. Choqlillo ");
        ingredientes1.add("400 gr. Cebolla ");
        ingredientes1.add("200 gr. Zanahoria ");
        ingredientes1.add("150 gr. Morrón ");
        ingredientes1.add("4 gr. Ajo ");
        ingredientes1.add("300 gr. Tomate ");
        ingredientes1.add("250 gr. Vino blanco ");
        ingredientes1.add("Especias (15 gr. Orégano, Ají de Color, Pimienta - 8 gr. Comino)");
        ingredientes1.add("al gusto Sal ");
        List<Instruccion> instrucciones1 = new ArrayList<>();
        instrucciones1.add(new Instruccion(1, "Cortar la carne en trozos de 250 gr., la cebolla en cuartos, la zanahoria en trozos, el tomate en gajos. Reservar"));
        instrucciones1.add(new Instruccion(2, "Calentar una olla a presión y adicionar aceite a punto humo, luego agregar los trozos de carne y sellar. Luego adicionar las verduras y seguir sofriendo por alrededorde 15 minutos."));
        instrucciones1.add(new Instruccion(3, "Una vez terminado el sofrito, adicionar la sal y las especias, continuar sofriendo por 5 minutos más."));
        instrucciones1.add(new Instruccion(4, "Pasado el tiempo, adicionar el vino blanco y dejar que de hervor. Luego adicionar agua hasta cubrir."));
        instrucciones1.add(new Instruccion(5, "Tapar la olla y una vez que de hervor cocinar por 45 minutos a fuego bajo."));
        instrucciones1.add(new Instruccion(6, "Al completar el tiempo, apagar el fuego y dejar que continúe su cocción al vapor sin fuego."));
        instrucciones1.add(new Instruccion(7, "Destapar la olla y servir acompañado de la guarnición que más te guste."));
        recetas.add(new Receta(nextId++,"Carne Mechada ", ingredientes1, instrucciones1, ""));

        List<String> ingredientes2 = new ArrayList<>();
        ingredientes2.add("");
        List<Instruccion> instrucciones2 = new ArrayList<>();
        instrucciones2.add(new Instruccion(1, ""));
        recetas.add(new Receta(nextId++,"Pastel de papa", ingredientes2, instrucciones2, ""));

        List<String> ingredientes3 = new ArrayList<>();
        ingredientes3.add("");
        List<Instruccion> instrucciones3 = new ArrayList<>();
        instrucciones3.add(new Instruccion(1, ""));
        recetas.add(new Receta(nextId++,"Pastel de Choclo", ingredientes3, instrucciones3, ""));
    }

    public List<Receta> obtenerRecetas() {
        return recetas;
    }

    public void crearReceta(Receta receta) {
        receta.setId(nextId++);
        recetas.add(receta);
    }

    public Receta obtenerRecetaPorId(Long id) {
        return recetas.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public void borrarReceta(Long id) {
        recetas.removeIf(p -> p.getId().equals(id));
    }

    public List<Receta> obtenerRecetaPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return recetas;
        }
        return recetas.stream()
                .filter(receta -> receta.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Flux<RecetaResponse> getAllRecetaService(){
        Flux<RecetaResponse> listRecetaResponse = webClient.get()
            .retrieve()
            .bodyToFlux(RecetaResponse.class)
            .switchIfEmpty(Flux.empty());;
        return listRecetaResponse;
    }
}
