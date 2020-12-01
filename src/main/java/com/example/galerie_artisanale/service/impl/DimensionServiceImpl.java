package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Dimension;
import com.example.galerie_artisanale.repository.DimensionRepository;
import com.example.galerie_artisanale.service.DimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DimensionRepository dimensionRepository;

    @Override
    public Dimension save(Dimension dimension) {
        return dimensionRepository.save(dimension);
    }

    @Override
    public Dimension findByDescription(String description) {
        return dimensionRepository.findByDescription(description);
    }

    @Override
    public List<Dimension> findAll() {
        return dimensionRepository.findAll();
    }

    @Override
    public Dimension findById(Long id) {
        Optional<Dimension> byId = dimensionRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void removeOne(Long id) {
        dimensionRepository.delete(findById(id));
    }


}
