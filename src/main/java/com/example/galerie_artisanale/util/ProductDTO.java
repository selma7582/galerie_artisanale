package com.example.galerie_artisanale.util;

import com.example.galerie_artisanale.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class ProductDTO {

    private String productName;
    private String description;
    private float price;
    private int inStockNumber;
    private Category category;
    private Shape shape;
    private Dimension dimension;
    private Provider provider;
    private List<Image> imagesList;


}
