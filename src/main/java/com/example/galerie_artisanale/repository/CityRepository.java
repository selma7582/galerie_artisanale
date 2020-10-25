package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Long> {

    List<City>findAll();

    City findByCityName(String cityName);

}
