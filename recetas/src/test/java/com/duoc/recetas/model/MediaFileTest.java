package com.duoc.recetas.model;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class MediaFileTest {

    @Test
    void testGettersAndSetters() {
        MediaFile mediaFile = new MediaFile();

        Long id = 1L;
        Long idReceta = 101L;
        String fileName = "receta.jpg";
        String fileType = "image/jpeg";
        Long fileSize = 2048L;
        byte[] fileData = { 1, 2, 3, 4, 5 };
        String description = "Una descripción de prueba";

        mediaFile.setId(id);
        mediaFile.setId_receta(idReceta);
        mediaFile.setFileName(fileName);
        mediaFile.setFileType(fileType);
        mediaFile.setFileSize(fileSize);
        mediaFile.setFileData(fileData);
        mediaFile.setDescription(description);

        assertEquals(id, mediaFile.getId());
        assertEquals(idReceta, mediaFile.getId_receta());
        assertEquals(fileName, mediaFile.getFileName());
        assertEquals(fileType, mediaFile.getFileType());
        assertEquals(fileSize, mediaFile.getFileSize());
        assertArrayEquals(fileData, mediaFile.getFileData());
        assertEquals(description, mediaFile.getDescription());
    }

    @Test
    void testDefaultConstructor() {
        MediaFile mediaFile = new MediaFile();

        assertNull(mediaFile.getId());
        assertNull(mediaFile.getId_receta());
        assertNull(mediaFile.getFileName());
        assertNull(mediaFile.getFileType());
        assertNull(mediaFile.getFileSize());
        assertNull(mediaFile.getFileData());
        assertNull(mediaFile.getDescription());
    }

}
