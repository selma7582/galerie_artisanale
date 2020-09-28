package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.ShoppingCart;
import com.example.galerie_artisanale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    ShoppingCart findByUser(User user);

}
