package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.UserRole;
import com.example.galerie_artisanale.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServices {

    @Autowired
    private UserRoleRepository userRoleRepository;


    public UserRole save(UserRole userRole){

        userRoleRepository.save(userRole);

        return userRole;



    }

    public  UserRole delete(UserRole userRole){

        userRoleRepository.delete(userRole);

        return userRole;
    }



}
