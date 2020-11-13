package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.CartItem;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByOrdered(Ordered ordered);


}
