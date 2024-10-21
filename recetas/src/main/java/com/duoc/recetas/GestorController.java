package com.duoc.recetas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duoc.recetas.service.RecetaService;

@Controller
public class GestorController {
    private final RecetaService recetaService;

    public GestorController(RecetaService recetaService){
        this.recetaService = recetaService;
    }

    @GetMapping("/gestor")
    public String gestor(Model model) {
        model.addAttribute("listarecetas", recetaService.obtenerRecetas());
        return "gestor";
    }

    @GetMapping("/agregar")
    public String agregar() {
        return "agregarreceta";
    }

    @GetMapping("/editar")
    public String edtar() {
        return "editarreceta";
    }

}
