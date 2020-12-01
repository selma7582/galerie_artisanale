package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.OrderedStatus;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.repository.OrderRepository;
import com.example.galerie_artisanale.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return byId.orElse(null);
    }

    @Override
    public Ordered findById(Long id) {
        Optional<Ordered> byId = orderRepository.findById(id);

        return byId.orElse(null);

    }



    @Override
    public List<Ordered> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Ordered findShoppingCart(User user) {
        List<Ordered> shoppingCarts = orderRepository.findByUserAndStatus(user,OrderedStatus.INVALID);
        if (shoppingCarts.size() >1 ){

            throw new IllegalStateException("We should have at most one invalid ordered");
        }else if (shoppingCarts.isEmpty()){
            return null ;
        }else {
            return shoppingCarts.get(0);
        }
    }

    @Override
    public List<Ordered> findByUserAndStatus(User user, OrderedStatus status){
        List<Ordered> orderedList = orderRepository.findByUserAndStatus(user,OrderedStatus.VALID);
        return orderedList;
    }

    @Override
    public void remove(Ordered persistedShoppingCart) {
        orderRepository.delete(persistedShoppingCart);
    }

}
