package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Category;
import com.example.galerie_artisanale.entity.City;

import java.util.List;

public interface CityService {

    City save(City city);

    List<City> findAll();

}
