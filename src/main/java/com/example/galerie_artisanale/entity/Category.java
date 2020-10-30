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

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Long id;
    @Column(columnDefinition = "text")
    private String categoryName;

    transient
    private boolean removed ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Product> productList;

    @Override
    public String toString() {
        return  categoryName ;
    }
}
