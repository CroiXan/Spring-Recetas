package com.duoc.recetas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecetasController {
    
    @GetMapping("/recetas")
    public String recetas(){
        return "recetas";
    }

    @GetMapping("/")
    public String raiz(){
        return "recetas";
    }
    
}
