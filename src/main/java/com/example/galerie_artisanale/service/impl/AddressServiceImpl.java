package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.repository.AddressRepository;
import com.example.galerie_artisanale.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }
}
