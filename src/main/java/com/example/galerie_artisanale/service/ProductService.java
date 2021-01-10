package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {


    Product save(Product product);

    List<Product> findAll();

    Product findById(Long id);

    Product findOne(Long id);

    void removeOne(Long id);

    Product findByProductName(String productName);

    List<Product> findByCategory(String category);
    Page<Product> findByCategory(String category,int pageNo, int pageSize);

    List<Product> findByDimension(String dimension);
    Page<Product> findByDimension(String dimension, int pageNo, int pageSize);

    List<Product> blurrySearch(String productName);

    List<Product> findByShape(String shape);
    Page<Product> findByShape(String shape, int pageNo, int pageSize);

    List<Product> filterProducts(int min, int max);

   // Page<Product> filterProducts(int min, int max,int pageNo, int pageSize);

    Page<Product> findPagination(int pageNo, int pageSize);



}
