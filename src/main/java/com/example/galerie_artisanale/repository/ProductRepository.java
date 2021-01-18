package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product>findAll();


    List<Product> findByProductNameContaining(String productName );


    List<Product> findByCategoryCategoryName(String category);
    Page<Product> findByCategoryCategoryName(String category, Pageable pageable);

    List<Product> findByDimensionDescription(String dimension);
    Page<Product> findByDimensionDescription(String dimension,Pageable pageable);

    List<Product> findByProvider(Long id);




    Page<Product> findByPrice( float price, Pageable pageable);

    @Query("select p from Product p where p.price BETWEEN :x  and :y ")
    Page<Product> chercher(

            @Param("x") float priceMin,
            @Param("y") float priceMax, Pageable pageable);



    List<Product> findByShapeShapeName(String shape);
    Page<Product> findByShapeShapeName(String shape,Pageable pageable);





    Product findByProductName(String productName);
}
