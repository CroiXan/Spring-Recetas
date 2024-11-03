package com.duoc.recetas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.duoc.recetas.model.IngredienteResponse;
import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GestorController {

    private RecetaResponse selectedReceta;
    private List<RecetaResponse> listaRecetas;
    private final RecetaService recetaService;

    public GestorController(RecetaService recetaService){
        this.recetaService = recetaService;
    }

    @GetMapping("/gestor")
    public String gestor(Model model) {
        Flux<RecetaResponse> fluxRecetaResponse = recetaService.getAllRecetaService();
        listaRecetas = fluxRecetaResponse.collectList().block();
        model.addAttribute("listarecetas", listaRecetas);
        return "gestor";
    }

    @GetMapping("/agregar")
    public String agregar() {
        return "agregarreceta";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        selectedReceta = new RecetaResponse();
        Optional<RecetaResponse> resultReceta = listaRecetas.stream().filter(receta -> receta.getId_receta() == id ).findFirst();

        if (resultReceta.isPresent()) {
            selectedReceta = resultReceta.get();
        }
        model.addAttribute("receta", selectedReceta);

        return "editarreceta";
    }

    @PostMapping("/saveedit")
    public String guardarEditar(Model model) {

        return "gestor";
    }
    
    @PostMapping("/addingrediente/{id}")
    public String addIngrediente(@PathVariable("id") Long id, @RequestParam("descripcion") String descripcion) {
        IngredienteResponse request = new IngredienteResponse();
        request.setId_receta(id);
        request.setNombr_item(descripcion);
        Mono<IngredienteResponse> response = recetaService.addIngrediente(request);
        List<IngredienteResponse> listaIngredienteLocal = selectedReceta.getListaIngredientes();
        listaIngredienteLocal.add(response.block());
        selectedReceta.setListaIngredientes(listaIngredienteLocal);
        return "redirect:/editar/" + id;
    }
    
    @PostMapping("/editingrediente/{id}")
    public String editIngrediente(@PathVariable("id") Long id, @RequestParam String action) {
        if ("editar".equals(action)) {
        } else if ("eliminar".equals(action)) {
        }
        return "redirect:/editar/" + id;
    }
    
}
