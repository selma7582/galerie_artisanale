package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    //List<Category>findAll();

    Category findByCategoryName(String categoryName);


}
