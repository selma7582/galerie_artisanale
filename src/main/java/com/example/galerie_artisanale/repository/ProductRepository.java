package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.function.Supplier;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product>findAll();


    List<Product> findAllById(Long id);

/*
    List<Product> findByRemovedFalse();
        Category findByCategoryName(String categoryName);

*/


    List<Product> findByCategoryCategoryName(String category);


    Product findByProductName(String productName);
}
