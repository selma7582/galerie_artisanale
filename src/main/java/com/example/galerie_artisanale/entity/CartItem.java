package com.example.galerie_artisanale.entity;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qty;
    private float buyinPrice;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name="shopping_cart_id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Ordered ordered;

    @Override
    public int hashCode(){
        return super.hashCode();
    }


}
