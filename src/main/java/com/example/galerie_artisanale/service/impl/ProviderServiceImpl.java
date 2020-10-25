package com.example.galerie_artisanale.service.impl;

import com.example.galerie_artisanale.entity.Provider;
import com.example.galerie_artisanale.repository.ProviderRepository;
import com.example.galerie_artisanale.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;


    @Override
    public Provider save(Provider provider) {
        return providerRepository.save(provider);
    }

    @Override
    public List<Provider> findAll() {
        return  (List<Provider>)providerRepository.findAll();

    }

    @Override
    public Provider findById(Long id) {

        Optional<Provider> byId = providerRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public void removeOne(Long id) {
        providerRepository.deleteById(id);
    }

    @Override
    public Provider findByEmail(String email) {
        return providerRepository.findByEmail(email);
    }


    @Override
    public Provider findOne(Long id) {
        Optional<Provider> byId = providerRepository.findById(id);
        return byId.orElse(null);
    }


    @Override
    public void delete(Provider provider) {
        providerRepository.delete(provider);
    }


}

