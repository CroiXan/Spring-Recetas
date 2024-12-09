package com.example.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.example.users.models.MediaFile;
import com.example.users.repository.MediaFileRepository;

public class MediaFileServiceTest {

    @InjectMocks
    private MediaFileService mediaFileService;

    @Mock
    private MediaFileRepository repository;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveFile_ShouldSaveAndReturnMediaFile() throws IOException {
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getContentType()).thenReturn("text/plain");
        when(mockFile.getSize()).thenReturn(123L);
        when(mockFile.getBytes()).thenReturn("test content".getBytes());

        MediaFile savedFile = new MediaFile();
        savedFile.setFileName("test.txt");
        savedFile.setFileType("text/plain");
        savedFile.setFileSize(123L);
        when(repository.save(any(MediaFile.class))).thenReturn(savedFile);

        MediaFile result = mediaFileService.saveFile(mockFile, "Test description", 1L);

        assertNotNull(result);
        assertEquals("test.txt", result.getFileName());
        assertEquals("text/plain", result.getFileType());
        assertEquals(123L, result.getFileSize());
        verify(repository, times(1)).save(any(MediaFile.class));
    }

    @Test
    void getAllFiles_ShouldReturnListOfFiles() {
        MediaFile file1 = new MediaFile();
        MediaFile file2 = new MediaFile();
        when(repository.findAll()).thenReturn(Arrays.asList(file1, file2));

        List<MediaFile> result = mediaFileService.getAllFiles();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllFilesByRecetaId_ShouldReturnFilteredAndSortedFiles() {
        MediaFile file1 = new MediaFile();
        file1.setId(1L);
        MediaFile file2 = new MediaFile();
        file2.setId(2L);

        when(repository.findById_receta(1L)).thenReturn(Arrays.asList(file1, file2));

        List<MediaFile> result = mediaFileService.getAllFilesByRecetaId(1L);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(repository, times(1)).findById_receta(1L);
    }

    @Test
    void getFile_ShouldReturnMediaFileWhenFound() {
        MediaFile file = new MediaFile();
        when(repository.findById(1L)).thenReturn(Optional.of(file));

        MediaFile result = mediaFileService.getFile(1L);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getFile_ShouldThrowExceptionWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> mediaFileService.getFile(1L));

        assertEquals("File not found", exception.getMessage());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deleteFile_ShouldDeleteMediaFileById() {
        mediaFileService.deleteFile(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void updateDescription_ShouldUpdateAndReturnUpdatedMediaFile() {
        MediaFile file = new MediaFile();
        file.setDescription("Old description");
        when(repository.findById(1L)).thenReturn(Optional.of(file));
        when(repository.save(file)).thenReturn(file);

        MediaFile result = mediaFileService.updateDescription(1L, "New description");

        assertNotNull(result);
        assertEquals("New description", result.getDescription());
        verify(repository, times(1)).save(file);
    }
    
}
