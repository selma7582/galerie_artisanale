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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="order_id")
    private Ordered ordered;

    @Override
    public int hashCode(){
        return super.hashCode();
    }


    public String getFirstImageFullUrl(){
        if (product == null || product.getImagesList() == null || product.getImagesList().isEmpty()){
            return null ;
        }else {
            return product.getImagesList().get(0).getFullURL();
        }
    }



}
