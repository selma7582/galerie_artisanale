package com.example.galerie_artisanale.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double GrandTotal;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Ordered ordered;

    @Column(name ="dateCreation")
    private Date creationDate;

    @Column(name ="dateModification")
    private Date updateDate;




    public double getTotal(){
       final List<CartItem> cartItemList  = ordered != null ?ordered.getCartItemList(): null ;
        if (cartItemList != null && !cartItemList.isEmpty()){
            return  cartItemList.stream()
                    .mapToDouble(item -> item.getQty() * item.getProduct().getPrice())
                    .sum();
        }else {
            return 0d ;
        }


    }

    public int getCartItemCount(){
        final List<CartItem> cartItemList  = ordered != null ?ordered.getCartItemList(): null ;

        if (cartItemList != null && !cartItemList.isEmpty()){
            return  cartItemList.stream()
                    .mapToInt(item -> item.getQty())
                    .sum();
        }else {
            return 0 ;
        }
    }

    public List<CartItem>  getCartItemList() {
        return ordered != null ?ordered.getCartItemList(): new ArrayList<>();
    }



}
