package com.example.galerie_artisanale.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
/*
    private Date shippingDate;
*/
    private String orderStatus;
    private String status;


    @OneToMany(mappedBy = "ordered", cascade=CascadeType.ALL )
    private List<CartItem> cartItemList = new ArrayList<>();

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn( name = "ship_address")
    private Address shippingAddress;

    @ManyToOne
    @JoinColumn( name = "bill_address")
    private Address billingAddress;

    @OneToOne (mappedBy = "ordered")
    private ShoppingCart shoppingCart;


}
