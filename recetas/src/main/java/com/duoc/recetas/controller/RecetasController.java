package com.duoc.recetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.duoc.recetas.model.Receta;
import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.security.SeguridadUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.duoc.recetas.service.MediaFileService;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class RecetasController {
    private final RecetaService recetaService;
    private final MediaFileService mediaFileService;

    public RecetasController(RecetaService recetaService, MediaFileService mediaFileService){
        this.recetaService = recetaService;
        this.mediaFileService = mediaFileService;
    }
    
    @GetMapping("/recetas")
    public String recetas(Model model){
        Flux<RecetaResponse> fluxRecetaResponse = recetaService.getAllRecetaService();
        List<RecetaResponse> listaRecetaResponse = fluxRecetaResponse.collectList().block();

        Map<Long, String> imagenes = listaRecetaResponse.stream()
                .collect(Collectors.toMap(
                    RecetaResponse::getId_receta,
                        receta -> mediaFileService.getImageUrlForReceta(receta.getId_receta()).block()
                ));
        model.addAttribute("imagenes", imagenes);

        model.addAttribute("listarecetas", listaRecetaResponse);
        return "recetas";
    }

    @GetMapping("/busqueda")
    public String busqueda(Model model, @RequestParam(value = "search", required = false) String search){
        //String sanitizedSearch = SeguridadUtil.sanitizarParaHTML(search);
        Flux<RecetaResponse> fluxRecetas = recetaService.getRecetasByNameService(search);
        List<RecetaResponse> listaRecetaResponse = fluxRecetas.collectList().block();
        model.addAttribute("listarecetas", listaRecetaResponse);
        model.addAttribute("search", search);
        return "busqueda";
    }

    @GetMapping("/")
    public String raiz(Model model){
        Flux<RecetaResponse> fluxRecetaResponse = recetaService.getAllRecetaService();
        List<RecetaResponse> listaRecetaResponse = fluxRecetaResponse.collectList().block();
        Map<Long, String> imagenes = listaRecetaResponse.stream()
                .collect(Collectors.toMap(
                    RecetaResponse::getId_receta,
                        receta -> mediaFileService.getImageUrlForReceta(receta.getId_receta()).block()
                ));
        model.addAttribute("imagenes", imagenes);
        model.addAttribute("listarecetas", listaRecetaResponse);
        return "recetas";
    }
    
}
