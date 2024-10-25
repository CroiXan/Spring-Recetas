package com.duoc.recetas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duoc.recetas.model.Receta;
import java.util.List;
import com.duoc.recetas.service.RecetaService;

@Controller
public class RecetasController {
    private final RecetaService recetaService;

    public RecetasController(RecetaService recetaService){
        this.recetaService = recetaService;
    }
    
    @GetMapping("/recetas")
    public String recetas(Model model){
        model.addAttribute("listarecetas", recetaService.obtenerRecetas());
        return "recetas";
    }

    @GetMapping("/busqueda")
    public String busqueda(Model model, @RequestParam(value = "search", required = false) String search){
        List<Receta> listaRecetas = recetaService.obtenerRecetaPorNombre(search);
        model.addAttribute("listarecetas", listaRecetas);
        model.addAttribute("search", search);
        return "busqueda";
    }

    @GetMapping("/")
    public String raiz(Model model){
        model.addAttribute("listarecetas", recetaService.obtenerRecetas());
        return "recetas";
    }
    
}
