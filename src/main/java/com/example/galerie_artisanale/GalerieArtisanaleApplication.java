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
//@ComponentScan("controller")
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
        user1.setEmail("admin@hotmail.com");
        System.out.println("coucou"+user1.getEmail());
        user1.setUsername("admin@hotmail.com");
        user1.setTel("0486938793");
        user1.setPassword(passwordEncoder.encode("admin"));
        userService.createUser(user1, "ROLE_ADMIN");

/**
 User user2 = new User();
 user2.setEmail("Peter@gmail.com");
 user2.setUsername("Peter");
 user2.setTel("04869754332");
 user2.setPassword(SecurityUtility.passwordEncoder().encode("p"));


 UserRole role2 = new UserRole();
 role2.setUserRoleId(user1.getId_user());
 role2.setRole("ROLE_ADMIN");

 userService.createUser(user2, role2);*/
        /*
        User user3 = new User();
        user3.setEmail("Peter@gmail.com");
        user3.setUsername("Peter");
        user3.setTel("04869754332");
        user3.setPassword(SecurityUtility.passwordEncoder().encode("p"));


        UserRole role2 = new UserRole();
        role2.setUserRoleId(user3.getId_user());
        role2.setRole("ROLE_ADMIN");
        Set<UserRole> userRoles = new HashSet<>();

        userRoles.add(new UserRole(user3.getId_user(),role2.getRole()));

        userService.createUser(user3, userRoles);*/

    }

}
