package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Image;
import com.example.galerie_artisanale.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.example.galerie_artisanale.controller.HomeController.fileToPath;


@Service
public class StorageServiceImpl implements StorageService {

    public static final String EMPTY_JPG = "images/common/not_found.jpeg";
    final Path rootLocation = Paths.get("images");

    @Override
    public void store(MultipartFile file, String newImageName) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new IllegalArgumentException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path resolve = rootLocation.resolve(newImageName);

                if (!Files.exists(resolve.getParent())) {
                    Files.createDirectories(resolve.getParent());
                }
                Files.copy(inputStream, resolve,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to store file " + filename, e);
        }
    }


    @Override
    public Path load(String filename) {
        return rootLocation.resolve( filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = Paths.get("").resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                if (!EMPTY_JPG.equals(filename)) {
                    return loadAsResource(EMPTY_JPG);
                }

                throw new IllegalArgumentException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Could not read file: " + filename, e);
        }
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);// create the root location if it is not exist
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not initialize storage", e);
        }
    }

    @Override
    public void fillFullImageUrl(Image img) {
        String imgUrl = img.getUrl_image();
        if (imgUrl == null) {
            imgUrl = "common/empty.jpeg";
        }
        img.setFullURL(fileToPath(load(imgUrl)));
    }

}
