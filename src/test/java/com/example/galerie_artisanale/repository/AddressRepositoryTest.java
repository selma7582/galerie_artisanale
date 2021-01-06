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
 class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;



    @Test
    public void testSaveNewAddress(){
        Address address = new Address();
        address.setStreet("My Street");
        City city = new City();
        city.setZipCode("1500");
        city.setCityName("Marrakech");
        City city1 = cityRepository.save(city);
        address.setCity(city1);
        address.setNumber("135d");
        addressRepository.save(address);
        List<Address> all = addressRepository.findAll();
        assertEquals(1,all.size());
        assertEquals("My Street",all.get(0).getStreet());
    }
    //@org.junit.jupiter.api.Test
*/
/*
    @Test
    void save() {
        addressRepository.deleteAll();
        Address  address = new Address();
        address.setNumber("14");
        City city = new City();
        city.setCityName("Agadir");
        city.setZipCode("1033");
        City persestedCity = cityRepository.save(city);
        address.setCity(persestedCity);
        address.setStreet("Street winner");
        addressRepository.save(address);

        // fetch all the adresses
        List<Address> all = addressRepository.findAll();
        assertEquals(1,all.size());

    }
    @Test
    void findByUser() {
    }

*//*


}
*/
