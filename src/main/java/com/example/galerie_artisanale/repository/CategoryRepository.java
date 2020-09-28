package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category>findAll();

    Category findByCategoryName(String categoryName);



   // List<Category> findByRemovedFalse();
}
