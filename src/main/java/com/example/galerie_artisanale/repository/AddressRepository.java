package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Provider;
import com.example.galerie_artisanale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address>findAll();

    List<Address> findByUser(User user);

    List<Address> findByProvider(Provider provider);

/*
    List<Address> findByOrdered(Ordered ordered);
*/




}
