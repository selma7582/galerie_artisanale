package com.example.galerie_artisanale.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Ordered {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Date orderDate;
    private Date shippingDate;
    private Date billingDate;
    private String shippingMethod;
    private String orderStatus;
    private double orderTotal;
    private boolean enabled;
    private String status;


    @OneToMany(mappedBy = "ordered", cascade=CascadeType.ALL )
    private List<CartItem> cartItemList;

    @ManyToOne
    private User user;

    @ManyToOne
    private Address shippingAddress;

    @ManyToOne
    private Address billingAddress;

    @OneToOne(targetEntity = ShoppingCart.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private ShoppingCart shoppingCart;


}
