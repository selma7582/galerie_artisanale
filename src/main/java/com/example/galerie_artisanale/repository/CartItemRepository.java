package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.CartItem;
import com.example.galerie_artisanale.entity.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findByOrdered(Ordered ordered);


}
