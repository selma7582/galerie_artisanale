package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShapeRepository extends JpaRepository<Shape,Long> {

    List<Shape>findAll();

    Shape findByShapeName(String ShapeName);


}
