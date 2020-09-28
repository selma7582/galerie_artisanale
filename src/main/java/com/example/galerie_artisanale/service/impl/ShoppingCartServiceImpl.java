package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.CartItem;
import com.example.galerie_artisanale.entity.ShoppingCart;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.repository.ShoppingCartRepository;
import com.example.galerie_artisanale.service.CartItemService;
import com.example.galerie_artisanale.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;


    @Autowired
    private CartItemService cartItemService;


/*
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {

        double cartTotal = 0;

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getProduct().getInStockNumber() > 0) {
                cartItemService.updateCartItem(cartItem);

                cartTotal += cartItem.getSubTotal();
            }
        }

        shoppingCart.setGrandTotal(cartTotal);

        shoppingCartRepository.save(shoppingCart);

        return shoppingCart;
    }


    public void clearShoppingCart(ShoppingCart shoppingCart) {
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for (CartItem cartItem : cartItemList) {
            cartItem.setShoppingCart(null);
            cartItemService.save(cartItem);
        }

        shoppingCart.setGrandTotal(0);

        shoppingCartRepository.save(shoppingCart);
    }

*/

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findByUser(User user) {
        return shoppingCartRepository.findByUser(user);
    }

    @Override
    public void remove(ShoppingCart persistedShoppingCart) {
        persistedShoppingCart.getCartItemList().stream()
                .forEach(cartItemService::remove);
        shoppingCartRepository.deleteById(persistedShoppingCart.getId());

    }
}
