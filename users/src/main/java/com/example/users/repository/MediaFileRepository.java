package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.users.models.MediaFile;
import java.util.List;


public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

    @Query("SELECT mf FROM media_file mf WHERE mf.id_receta = :id_receta")
    List<MediaFile> findById_receta(Long id_receta);

}
