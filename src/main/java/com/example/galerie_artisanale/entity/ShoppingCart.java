package com.example.galerie_artisanale.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double GrandTotal;

    @OneToMany(mappedBy="shoppingCart")
    @JsonIgnore
    private List<CartItem> cartItemList;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



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
