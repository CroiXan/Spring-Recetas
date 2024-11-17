package com.example.users.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.users.models.MediaFile;
import com.example.users.service.MediaFileService;

@RestController
@RequestMapping("/api/media")
public class MediaFileController {

    private final MediaFileService service;

    public MediaFileController(MediaFileService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("id_receta") Long id_receta) {
        try {
            MediaFile mediaFile = service.saveFile(file, description, id_receta);
            return new ResponseEntity<>(mediaFile, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        MediaFile mediaFile = service.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaFile.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(mediaFile.getFileType()))
                .body(mediaFile.getFileData());
    }

    @GetMapping("/receta/{id}")
    public ResponseEntity<byte[]> downloadFileByRecetaId(@PathVariable Long idReceta) {
        List<MediaFile> mediaFiles = service.getAllFilesByRecetaId(idReceta);

        if(mediaFiles.size() <= 0){
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + mediaFiles.get(0).getFileName() + "\"")
                .contentType(MediaType.parseMediaType(mediaFiles.get(0).getFileType()))
                .body(mediaFiles.get(0).getFileData());
    }

}
