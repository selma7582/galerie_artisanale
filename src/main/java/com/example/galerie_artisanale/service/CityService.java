package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.City;

import java.util.List;

public interface CityService {

    City save(City city);

    List<City> findAll();

    City findById(Long id);

    //City findByAddress(Address address);


}
