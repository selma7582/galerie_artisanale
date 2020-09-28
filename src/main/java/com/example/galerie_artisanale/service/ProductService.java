package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {


    Product save(Product product);

    List<Product> findAll();

    Product findById(Long id);

     Product findOne(Long id);

    void removeOne(Long id);
}
