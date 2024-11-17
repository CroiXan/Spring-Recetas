package com.example.users.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.users.models.MediaFile;
import com.example.users.repository.MediaFileRepository;

@Service
public class MediaFileService {

    private final MediaFileRepository repository;

    public MediaFileService(MediaFileRepository repository) {
        this.repository = repository;
    }

    public MediaFile saveFile(MultipartFile file, String description, Long idReceta) throws IOException {
        MediaFile mediaFile = new MediaFile();
        mediaFile.setId_receta(idReceta);
        mediaFile.setFileName(file.getOriginalFilename());
        mediaFile.setFileType(file.getContentType());
        mediaFile.setFileSize(file.getSize());
        mediaFile.setFileData(file.getBytes());
        mediaFile.setDescription(description);

        return repository.save(mediaFile);
    }

    public List<MediaFile> getAllFiles() {
        return repository.findAll();
    }

    public List<MediaFile> getAllFilesByRecetaId(Long idReceta) {
        return repository.findById_receta(idReceta);
    }

    public MediaFile getFile(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
    }

    public void deleteFile(Long id) {
        repository.deleteById(id);
    }

    public MediaFile updateDescription(Long id, String description) {
        MediaFile mediaFile = getFile(id);
        mediaFile.setDescription(description);
        return repository.save(mediaFile);
    }
    
}
