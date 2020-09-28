package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.OrderRepository;
import com.example.galerie_artisanale.service.CartItemService;
import com.example.galerie_artisanale.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Ordered save(Ordered order) {
       return orderRepository.save(order);
    }

    @Override
    public List<Ordered> findAll() {
        return (List<Ordered>) orderRepository.findAll();
    }

    @Override
    public void removeOne(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Ordered findOne(Long id) {
        Optional<Ordered> byId = orderRepository.findById(id);

        return byId.orElse(null);//.findOne(id);
    }

    @Override
    public Ordered findById(Long id) {
        Optional<Ordered> byId = orderRepository.findById(id);

        return byId.orElse(null);

    }

/*    @Override
    public Ordered createOrdered(ShoppingCart shoppingCart, User user) {


            *//*    ShippingAddress shippingAddress,
                BillingAddress billingAddress,*//*

            Ordered ordered = new Ordered();
*//*
            order.setBillingAddress(billingAddress);
*//*
            ordered.setOrderStatus("created");
*//*
            ordered.setShippingAddress(shippingAddress);
*//*

            List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

            for (CartItem cartItem : cartItemList) {
                Product product = cartItem.getProduct();
                cartItem.setOrder(ordered);
                product.setInStockNumber(product.getInStockNumber() - cartItem.getQty());
            }

            ordered.setCartItemList(cartItemList);
            ordered.setOrderDate(Calendar.getInstance().getTime());
*//*
            ordered.setOrderTotal(shoppingCart.getGrandTotal());
*//*
            *//*shippingAddress.setOrder(ordered);
            billingAddress.setOrder(ordered);*//*
            ordered.setUser(user);
            ordered = orderRepository.save(ordered);

            return ordered;
        }*/

    @Override
    public List<Ordered> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

}
