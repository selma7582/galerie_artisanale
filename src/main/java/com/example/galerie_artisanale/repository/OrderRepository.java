package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Ordered,Long> {
    List<Ordered> findAll();
    List<Ordered> findByUser(User user);



}
