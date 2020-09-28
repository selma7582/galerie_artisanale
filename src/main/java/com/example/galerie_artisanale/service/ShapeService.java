package com.example.galerie_artisanale.service;



import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Shape;

import java.util.List;
import java.util.Optional;

public interface ShapeService {

    Shape save(Shape shape);

    List<Shape> findAll();

    Shape findByShapeName(String shapeName);

    Shape findById(Long id);

    void removeOne(Long id);




}
