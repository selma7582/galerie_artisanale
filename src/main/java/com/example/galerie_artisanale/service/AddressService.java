package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Address;

import java.util.List;

public interface AddressService {

    Address save(Address address);

    List<Address> findAll();
}
