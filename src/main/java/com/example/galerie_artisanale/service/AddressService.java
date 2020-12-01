package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Provider;
import com.example.galerie_artisanale.entity.User;

import java.util.List;

public interface AddressService {

    Address save(Address address);

    Address findOne(Long  id);

    List<Address> findAll();

    List<Address> findByUser(User user);

    List<Address> findByProvider(Provider provider);

/*
    List<Address> findByOrdered(Ordered ordered);
*/
}
