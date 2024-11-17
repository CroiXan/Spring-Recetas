package com.duoc.recetas.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.duoc.recetas.model.IngredienteResponse;
import com.duoc.recetas.model.InstruccionResponse;
import com.duoc.recetas.model.RecetaRequest;
import com.duoc.recetas.model.RecetaResponse;
import com.duoc.recetas.service.MediaFileService;
import com.duoc.recetas.service.RecetaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GestorController {

    private RecetaResponse selectedReceta;
    private List<RecetaResponse> listaRecetas;
    private final RecetaService recetaService;
    private final MediaFileService mediaFileService;

    public GestorController(RecetaService recetaService, MediaFileService mediaFileService) {
        this.recetaService = recetaService;
        this.mediaFileService = mediaFileService;
    }

    @GetMapping("/gestor")
    public String gestor(Model model) {
        Flux<RecetaResponse> fluxRecetaResponse = recetaService.getAllRecetaService();
        listaRecetas = fluxRecetaResponse.collectList().block();
        model.addAttribute("listarecetas", listaRecetas);
        return "gestor";
    }

    @PostMapping("/agregarreceta")
    public String agregarReceta(@RequestParam("nombreReceta") String nombreReceta) {
        RecetaRequest request = new RecetaRequest();
        request.setNombre(nombreReceta);
        recetaService.addReceta(request).block();
        return "redirect:/gestor";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        selectedReceta = new RecetaResponse();
        Mono<RecetaResponse> monoRecetaResponse = recetaService.getRecetaById(id);
        selectedReceta = monoRecetaResponse.block();
        model.addAttribute("receta", selectedReceta);
        return "editarreceta";
    }

    @PostMapping("/saveedit")
    public String guardarEditar(@RequestParam("recetaId") Long recetaId,
            @RequestParam("nombreReceta") String nombreReceta) {
        RecetaResponse getReceta = recetaService.getRecetaById(recetaId).block();
        getReceta.setNombre(nombreReceta);
        recetaService.editarReceta(getReceta);
        return "redirect:/editar/" + recetaId;
    }

    /*@PostMapping("/savefile")
    public String guardarArchivo(
            @RequestParam("recetaId") Long recetaId,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("archivo") MultipartFile archivo,
            Model model) {

        try {
            if (!archivo.isEmpty()) {
                
                Path tempFile = Paths.get(System.getProperty("java.io.tmpdir"), archivo.getOriginalFilename());
                archivo.transferTo(tempFile.toFile());

                String response = mediaFileService.uploadFile(tempFile, "Archivo para la receta: " + nombre,recetaId).block();

                File tempFileToDelete = tempFile.toFile();
                if (tempFileToDelete.exists()) {
                    tempFileToDelete.delete();
                }

                System.out.println("Archivo subido con éxito: " + response);
            }

            model.addAttribute("message", "Receta actualizada correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la receta: " + e.getMessage());
        }

        return "redirect:/editar/" + recetaId;
    }*/

    @PostMapping("/addingrediente/{id}")
    public String addIngrediente(@PathVariable("id") Long id,
            @RequestParam("descripcion") String descripcion) {
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
    public String editIngrediente(@PathVariable("id") Long id,
            @RequestParam Long ingredienteId,
            @RequestParam String descripcion,
            @RequestParam String action) {

        if ("editar".equals(action)) {
            IngredienteResponse getIngrediente = recetaService.getIngredienteById(ingredienteId).block();
            getIngrediente.setNombr_item(descripcion);
            recetaService.editarIngrediente(getIngrediente);
        } else if ("eliminar".equals(action)) {
            recetaService.eliminarIngrediente(ingredienteId);
        }

        return "redirect:/editar/" + id;
    }

    @PostMapping("/editinstruccion/{id}")
    public String editarInstruccion(@PathVariable Long id,
            @RequestParam Long instruccionId,
            @RequestParam String descripcion,
            @RequestParam Integer posicion,
            @RequestParam String action) {

        if ("editar".equals(action)) {
            InstruccionResponse getInstruccion = recetaService.getInstruccionById(instruccionId).block();
            getInstruccion.setDescripcion(descripcion);
            getInstruccion.setPosicion(posicion);
            recetaService.editarInstruccion(getInstruccion);
        } else if ("eliminar".equals(action)) {
            recetaService.eliminarInstruccion(instruccionId);
        }

        return "redirect:/editar/" + id;
    }

    @PostMapping("/addinstruccion/{id}")
    public String agregarInstruccion(@PathVariable Long id,
            @RequestParam String descripcion,
            @RequestParam Integer posicion) {
        recetaService.agregarInstruccion(id, descripcion, posicion);
        return "redirect:/editar/" + id;
    }

}
