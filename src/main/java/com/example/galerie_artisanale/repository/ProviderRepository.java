package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider,Long> {


   //     List<Product> findByCategoryCategoryName(String category);



    Provider findByEmail(String email);



}
