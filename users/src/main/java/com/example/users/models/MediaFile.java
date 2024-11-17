package com.example.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "media_file")
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long id_receta;
    private String fileName;
    private String fileType;
    private Long fileSize;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Lob
    private String description;
    
}
