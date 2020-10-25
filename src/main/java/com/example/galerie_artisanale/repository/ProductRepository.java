package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product>findAll();


    List<Product> findAllById(Long id);


    List<Product> findByCategoryCategoryName(String category);

    List<Product> findByProvider(Long id);


    Product findByProductName(String productName);
}
