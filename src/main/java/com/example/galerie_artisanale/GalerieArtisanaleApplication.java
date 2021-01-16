package com.example.galerie_artisanale;

import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.security.SecurityUtility;
import com.example.galerie_artisanale.entity.UserRole;
import com.example.galerie_artisanale.service.StorageService;
import com.example.galerie_artisanale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class GalerieArtisanaleApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService ;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    public static void main(String[] args) {
        SpringApplication.run(GalerieArtisanaleApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        storageService.init();
        User user1 = new User();
        user1.setEnabled(1);
        user1.setEmail("galerie@hotmail.com");
        user1.setTel("0486938793");
        user1.setPassword(passwordEncoder.encode("admin"));
        userService.createUser(user1, "ROLE_ADMIN");





    }

}
