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
    // the date of confirmation of the order
    private Date orderDate;
    /*
        private Date shippingDate;
    */
    @Enumerated(EnumType.STRING)
    private OrderedStatus status = OrderedStatus.INVALID;


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

    public double getTotal(){
        if (cartItemList != null && !cartItemList.isEmpty()){
            return  cartItemList.stream()
                    .mapToDouble(item -> item.getQty() * item.getProduct().getPrice())
                    .sum();
        }else {
            return 0d ;
        }


    }

    public int getCartItemCount(){

        if (cartItemList != null && !cartItemList.isEmpty()){
            return  cartItemList.stream()
                    .mapToInt(item -> item.getQty())
                    .sum();
        }else {
            return 0 ;
        }
    }


}
