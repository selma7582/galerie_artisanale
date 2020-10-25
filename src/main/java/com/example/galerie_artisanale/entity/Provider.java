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
public class Provider implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname")
    private String firstName;


    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "tel")
    private String tel;



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private List<Product> productList;

    @Transient
    private boolean removed ;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private List<Address> addressList;


}
