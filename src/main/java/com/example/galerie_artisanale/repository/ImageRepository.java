package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {

}
