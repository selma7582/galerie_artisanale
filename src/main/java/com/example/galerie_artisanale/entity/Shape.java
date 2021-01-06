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
public class Shape implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shape_id")
    private Long id;
    @Column(columnDefinition = "text")
    private String shapeName;

    @Transient
    private boolean removed ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shape")
    private List<Product> productList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shape")
    private List<Dimension> dimensionList;

    @Override
    public String toString() {
        return  shapeName ;
    }
}
