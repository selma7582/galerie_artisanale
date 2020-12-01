package com.example.galerie_artisanale.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "productName")
    private String productName;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "height")
    private float height;

    @Column(name = "width")
    private float width;

    @Column(name = "inStockNumber")
    private int inStockNumber;

    /*@Column(name = "active")
    private boolean active= true;*/

    @Transient
    private boolean removed ;

    @Transient
    private boolean selected ;


    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "id_shape")
    private Shape shape;

    @ManyToOne
    @JoinColumn(name = "id_dimension")
    private Dimension dimension;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Image> imagesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<CartItem> cartItemList;

    public String getMainImage(){

        if (imagesList != null && !imagesList.isEmpty()){
            return imagesList.get(0).getFullURL();
        }
        return null ;
    }

    public boolean haveImages(){
        return imagesList != null && !imagesList.isEmpty() ;
    }

    public String toString(){
        return productName;
    }

}
