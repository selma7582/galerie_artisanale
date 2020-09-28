package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void init();

    void store(MultipartFile file, String productID);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void fillFullImageUrl(Image img);
}
