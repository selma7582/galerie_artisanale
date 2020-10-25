package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address>findAll();


}
