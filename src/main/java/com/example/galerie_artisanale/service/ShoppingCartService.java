package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.ShoppingCart;
import com.example.galerie_artisanale.entity.User;


public interface ShoppingCartService {

    ShoppingCart save(ShoppingCart shoppingCart);

    ShoppingCart findByUser(User user);

    void remove(ShoppingCart persistedShoppingCart);
/*
    ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

    void clearShoppingCart(ShoppingCart shoppingCart);*/


}
