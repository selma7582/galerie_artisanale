package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CartItemService {

    CartItem save(CartItem cartItem);

    CartItem updateCartItem(CartItem cartItem);


    List<CartItem> findByOrdered(Ordered ordered);

    CartItem findById(Long id);


    List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

    void remove(CartItem cartItem);


    void removeOne(Long id);


/*
    void removeCartItem(CartItem cartItem);
*/



}
