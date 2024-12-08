package com.example.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.users.models.MediaFile;
import com.example.users.service.MediaFileService;

public class MediaFileControllerTest {

    @InjectMocks
    private MediaFileController mediaFileController;

    @Mock
    private MediaFileService mediaFileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile_ShouldReturnCreatedMediaFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String description = "Test description";
        Long idReceta = 1L;
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileName("test.txt");

        when(mediaFileService.saveFile(file, description, idReceta)).thenReturn(mediaFile);

        ResponseEntity<?> response = mediaFileController.uploadFile(file, description, idReceta);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mediaFile, response.getBody());
        verify(mediaFileService, times(1)).saveFile(file, description, idReceta);
    }

    @Test
    void uploadFile_ShouldReturnInternalServerErrorWhenIOExceptionOccurs() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String description = "Test description";
        Long idReceta = 1L;

        when(mediaFileService.saveFile(file, description, idReceta)).thenThrow(IOException.class);

        ResponseEntity<?> response = mediaFileController.uploadFile(file, description, idReceta);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(mediaFileService, times(1)).saveFile(file, description, idReceta);
    }

    @Test
    void downloadFile_ShouldReturnFileData() {
        Long fileId = 1L;
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileName("test.txt");
        mediaFile.setFileType("text/plain");
        mediaFile.setFileData("test content".getBytes());

        when(mediaFileService.getFile(fileId)).thenReturn(mediaFile);

        ResponseEntity<byte[]> response = mediaFileController.downloadFile(fileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mediaFile.getFileData(), response.getBody());
        assertEquals("attachment; filename=\"test.txt\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(mediaFileService, times(1)).getFile(fileId);
    }

    @Test
    void downloadFileByRecetaId_ShouldReturnFileData() {
        Long recetaId = 1L;
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileName("test.txt");
        mediaFile.setFileType("text/plain");
        mediaFile.setFileData("test content".getBytes());
        List<MediaFile> mediaFiles = Collections.singletonList(mediaFile);

        when(mediaFileService.getAllFilesByRecetaId(recetaId)).thenReturn(mediaFiles);

        ResponseEntity<byte[]> response = mediaFileController.downloadFileByRecetaId(recetaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mediaFile.getFileData(), response.getBody());
        assertEquals("attachment; filename=\"test.txt\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(mediaFileService, times(1)).getAllFilesByRecetaId(recetaId);
    }

    @Test
    void downloadFileByRecetaId_ShouldReturnNotFoundWhenNoFilesExist() {
        Long recetaId = 1L;

        when(mediaFileService.getAllFilesByRecetaId(recetaId)).thenReturn(Collections.emptyList());

        ResponseEntity<byte[]> response = mediaFileController.downloadFileByRecetaId(recetaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mediaFileService, times(1)).getAllFilesByRecetaId(recetaId);
    }

}
