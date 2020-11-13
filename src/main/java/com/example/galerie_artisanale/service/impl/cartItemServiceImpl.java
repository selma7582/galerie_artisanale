package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.CartItemRepository;
import com.example.galerie_artisanale.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class cartItemServiceImpl implements CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;


    @Override
    public List<CartItem> findAll() {
        return  cartItemRepository.findAll();
    }

    @Override
    public CartItem save(CartItem cartItem) {

        return cartItemRepository.save(cartItem);

    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
        return cartItem;
    }



    @Override
    public List<CartItem> findByOrdered(Ordered ordered) {
        return cartItemRepository.findByOrdered(ordered);
    }

    @Override
    public CartItem findById(Long id) {
        Optional<CartItem> byId = cartItemRepository.findById(id);
        return byId.orElse(null);
    }




    @Override
    public void remove(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }


    @Override
    public void removeOne(Long id) {
        cartItemRepository.deleteById(id);
    }



}
