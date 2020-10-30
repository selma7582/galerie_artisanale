package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.User;

import java.util.List;

public interface AddressService {

    Address save(Address address);

    List<Address> findAll();

    List<Address> findByUser(User user);
}
