package com.example.outsourcing.image.repository;

import com.example.outsourcing.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
