package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.repository.CategoryRepository;
import com.example.galerie_artisanale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public Category findById(Long id) {
        Optional <Category> byID = categoryRepository.findById(id);
        return byID.orElse(null);
    }



    @Override
    public void removeOne(Long id) {
        categoryRepository.deleteById(id);
    }



    @Override
    public Category findOne(Long id) {
        return null;
    }


}
