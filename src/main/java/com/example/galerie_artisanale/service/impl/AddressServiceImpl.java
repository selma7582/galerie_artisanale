package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Provider;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.repository.AddressRepository;
import com.example.galerie_artisanale.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address findOne(Long id) {
        Optional<Address> byId= addressRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public List<Address> findByUser(User user) {
        return addressRepository.findByUser(user);
    }

    @Override
    public List<Address> findByProvider(Provider provider) {
        return addressRepository.findByProvider(provider);
    }

    /*@Override
    public List<Address> findByOrdered(Ordered ordered ) {
        return addressRepository.findByOrdered(ordered);
    }*/

}
