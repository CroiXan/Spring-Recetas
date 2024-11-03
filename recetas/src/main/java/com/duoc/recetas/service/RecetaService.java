package com.duoc.recetas.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Collectors;

import com.duoc.recetas.model.IngredienteResponse;
import com.duoc.recetas.model.Instruccion;
import com.duoc.recetas.model.InstruccionResponse;
import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaRequest;
import com.duoc.recetas.model.RecetaResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecetaService {
    private List<Receta> recetas = new ArrayList<>();
    private Long nextId = 1L;

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
            .uri("receta")
            .retrieve()
            .bodyToFlux(RecetaResponse.class)
            .switchIfEmpty(Flux.empty());
        return listRecetaResponse;
    }

    public Mono<RecetaRequest> addReceta(RecetaRequest request){
        return this.webClient.post()
            .uri("receta")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RecetaRequest.class)
            .switchIfEmpty(Mono.empty());
    }

    public Mono<RecetaResponse> getRecetaById(Long recetaId){
        Mono<RecetaResponse> recetaResponse = webClient.get()
            .uri("receta/{id}",recetaId)
            .retrieve()
            .bodyToMono(RecetaResponse.class)
            .switchIfEmpty(Mono.empty());
        return recetaResponse;
    }

    public void editarReceta(RecetaResponse receta) {
        RecetaRequest request = new RecetaRequest();
        request.setId_receta(receta.getId_receta());
        request.setNombre(receta.getNombre());
        this.webClient.put()
            .uri("receta/{id}",request.getId_receta())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(RecetaResponse.class)
            .block();
    }

    public Mono<InstruccionResponse> getInstruccionById(Long instruccionId){
        Mono<InstruccionResponse> instruccionResponse = webClient.get()
            .uri("instrucciones/{id}",instruccionId)
            .retrieve()
            .bodyToMono(InstruccionResponse.class)
            .switchIfEmpty(Mono.empty());
        return instruccionResponse;
    }

    public Mono<IngredienteResponse> getIngredienteById(Long ingredienteId){
        Mono<IngredienteResponse> ingredienteResponse = webClient.get()
            .uri("ingredientes/{id}",ingredienteId)
            .retrieve()
            .bodyToMono(IngredienteResponse.class)
            .switchIfEmpty(Mono.empty());
        return ingredienteResponse;
    }

    public Mono<IngredienteResponse> addIngrediente(IngredienteResponse request){
        return this.webClient.post()
            .uri("ingredientes")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(IngredienteResponse.class);
    }

    public void editarIngrediente(IngredienteResponse request) {
        this.webClient.put()
            .uri("ingredientes/{id}",request.getId_ingrediente())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(IngredienteResponse.class)
            .block();
    }

    public void eliminarIngrediente(Long ingredienteId) {
        this.webClient.delete()
            .uri("ingredientes/{id}",ingredienteId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void editarInstruccion(InstruccionResponse request) {
        this.webClient.put()
            .uri("instrucciones/{id}",request.getId_instruccion())
            .bodyValue(request)
            .retrieve()
            .bodyToMono(InstruccionResponse.class)
            .block();
    }
    
    public void eliminarInstruccion(Long instruccionId) {
        this.webClient.delete()
            .uri("instrucciones/{id}",instruccionId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
    
    public void agregarInstruccion(Long recetaId, String descripcion, int posicion) {
        InstruccionResponse request = new InstruccionResponse();
        request.setId_receta(recetaId);
        request.setDescripcion(descripcion);
        request.setPosicion(posicion);
        this.webClient.post()
            .uri("instrucciones")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(InstruccionResponse.class)
            .block();
    }
}
