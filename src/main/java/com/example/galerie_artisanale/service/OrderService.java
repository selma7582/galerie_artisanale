package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.OrderedStatus;
import com.example.galerie_artisanale.entity.User;

import java.util.List;

public interface OrderService {

    Ordered save(Ordered order);

    List<Ordered> findAll();

    void removeOne(Long id) ;

    Ordered findOne(Long id);

    Ordered findById(Long id);


    List<Ordered> findByUser(User user);

    /**
     * The user must have at most one Ordered with status equals to {@link com.example.galerie_artisanale.entity.OrderedStatus#INVALID} which is the Shopping cart
     *
     * it throws IllegalStateException if there are more than one invalid ordered
     * @param user
     * @return
     */
    Ordered findShoppingCart(User user);

    void remove(Ordered persistedShoppingCart);

    List<Ordered> findByUserAndStatus(User user, OrderedStatus status);
}



