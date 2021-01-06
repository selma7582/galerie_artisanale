package com.example.galerie_artisanale.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
public class Dimension implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dimension")
    private Long id;
    @Column(columnDefinition = "text")
    private String description;

    transient
    private boolean removed ;

    @ManyToOne
    @JoinColumn(name = "id_shape")
    private Shape shape;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dimension")
    private List<Product> productList;

    @Override
    public String toString() {
        return  description ;
    }

}
