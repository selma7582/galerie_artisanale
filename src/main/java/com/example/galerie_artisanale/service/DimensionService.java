package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Dimension;

import java.util.List;

public interface DimensionService {

    Dimension save(Dimension dimension);

    Dimension findByDescription(String description);

    List<Dimension> findAll();

    Dimension findById(Long id);

    void removeOne(Long id);

    List<String> findAllDimensionDescription();
}
