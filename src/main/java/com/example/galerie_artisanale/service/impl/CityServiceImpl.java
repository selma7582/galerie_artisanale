package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.City;
import com.example.galerie_artisanale.repository.CityRepository;
import com.example.galerie_artisanale.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    CityRepository cityRepository;

    @Override
    public City save(City city) {
        return cityRepository.save(city);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }
}
