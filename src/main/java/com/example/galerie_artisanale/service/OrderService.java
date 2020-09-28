package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.ShoppingCart;
import com.example.galerie_artisanale.entity.User;

import java.util.List;

public interface OrderService {

    Ordered save(Ordered order);

    List<Ordered> findAll();

    void removeOne(Long id) ;

    Ordered findOne(Long id);

    Ordered findById(Long id);


    List<Ordered> findByUser(User user);


}



