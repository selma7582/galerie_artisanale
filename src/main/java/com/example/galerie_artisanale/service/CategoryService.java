package com.example.galerie_artisanale.service;


import com.example.galerie_artisanale.entity.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    List<Category> findAll();

    Category findByCategoryName(String categoryName);

    Category findById(Long id);

    void removeOne(Long id);


    Category findOne(Long id);


    List<String> findAllCategoryNames();
}
