package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Image;
import com.example.galerie_artisanale.repository.ImageRepository;
import com.example.galerie_artisanale.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImp implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public void removeOne(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public Image findById(Long id) {

        Optional<Image> byID = imageRepository.findById(id);
        return byID.orElse(null);

    }
}
