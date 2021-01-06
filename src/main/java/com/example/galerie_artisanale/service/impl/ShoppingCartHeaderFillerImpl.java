package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.controller.ShoppingCartController;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.service.OrderService;
import com.example.galerie_artisanale.service.ShoppingCartHeaderFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

import static com.example.galerie_artisanale.controller.ShoppingCartController.SHOPPING_CART_MODEL;


@Service
public class ShoppingCartHeaderFillerImpl implements ShoppingCartHeaderFiller {

    @Autowired
    private OrderService orderService;

    @Override
    public void fill(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (User.class.isInstance(authentication.getPrincipal())) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            model.addAttribute(SHOPPING_CART_MODEL, ordered);
        }else {
            Ordered  ordered = (Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
            model.addAttribute(SHOPPING_CART_MODEL, ordered);
        }
    }
}
