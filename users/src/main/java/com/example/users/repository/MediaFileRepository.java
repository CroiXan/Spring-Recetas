package com.example.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.users.models.MediaFile;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

}
