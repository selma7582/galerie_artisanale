package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Dimension;
import com.example.galerie_artisanale.entity.Shape;
import com.example.galerie_artisanale.repository.ShapeRepository;
import com.example.galerie_artisanale.service.ShapeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShapeServiceImp implements ShapeService {

    @Autowired
    ShapeRepository shapeRepository;

    @Override
    public Shape save(Shape shape) {
        return shapeRepository.save(shape);
    }

    @Override
    public List<Shape> findAll() {
        return shapeRepository.findAll();
    }

    @Override
    public Shape findByShapeName(String shapeName) {

        return shapeRepository.findByShapeName(shapeName);
    }

    @Override
    public Shape findById(Long id) {

        Optional<Shape> byId =  shapeRepository.findById(id);
        return byId.orElse(null);
    }
    @Override
    public void removeOne(Long id) {
        shapeRepository.deleteById(id);
    }


    @Override
    public List<String> findAllShapeName() {
        return findAll().stream().map(Shape::getShapeName).collect(Collectors.toList());
    }
}
