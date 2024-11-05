package com.duoc.recetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.security.SeguridadUtil;

import java.util.List;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;

@Controller
public class RecetasController {
    private final RecetaService recetaService;

    public RecetasController(RecetaService recetaService){
        this.recetaService = recetaService;
    }
    
    @GetMapping("/recetas")
    public String recetas(Model model){
        Flux<RecetaResponse> fluxRecetaResponse = recetaService.getAllRecetaService();
        List<RecetaResponse> listaRecetaResponse = fluxRecetaResponse.collectList().block();
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
        model.addAttribute("listarecetas", listaRecetaResponse);
        return "recetas";
    }
    
}
