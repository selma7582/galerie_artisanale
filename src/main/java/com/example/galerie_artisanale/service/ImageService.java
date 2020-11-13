package com.example.galerie_artisanale.service;


import com.example.galerie_artisanale.entity.Image;

import java.util.List;

public interface ImageService {

    Image save(Image image);

    List<Image> findAll();

    void removeOne(Long id) ;

    Image findById(Long id);



}
