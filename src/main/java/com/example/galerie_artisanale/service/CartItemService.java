package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CartItemService {

    List<CartItem>findAll();

    CartItem save(CartItem cartItem);

    CartItem updateCartItem(CartItem cartItem);

    List<CartItem> findByOrdered(Ordered ordered);

    CartItem findById(Long id);


    void remove(CartItem cartItem);

    void removeOne(Long id);



}
