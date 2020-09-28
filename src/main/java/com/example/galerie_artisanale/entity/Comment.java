package com.example.galerie_artisanale.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long id;
    @Column(columnDefinition = "text")
    private String description;


    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;


    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;




}
