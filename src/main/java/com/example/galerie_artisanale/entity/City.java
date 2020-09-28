package com.example.galerie_artisanale.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class City implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Long id;
    @Column(columnDefinition = "text")
    private String cityName;


   /* @ManyToOne
    @JoinColumn(name = "id_country")
    private Country country;*/


}
