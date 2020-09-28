package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Provider;

import java.util.List;
import java.util.Optional;


public interface ProviderService {

    Provider save(Provider provider);

    List<Provider> findAll();

    Provider findById(Long id);

    void delete(Provider provider);

    void removeOne(Long id) ;

}
