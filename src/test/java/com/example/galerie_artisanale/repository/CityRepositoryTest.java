/*
package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.City;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("test")
class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;




   @Test
    void save() {
        //addressRepository.deleteAll();
        City city = new City();
        city.setCityName("test");
        city.setZipCode("15330");
        cityRepository.save(city);

        // fetch all the adresses
        List<City> all = cityRepository.findAll();
        assertEquals(1,all.size());

    }





}
*/
