package com.example.galerie_artisanale.repository;


import com.example.galerie_artisanale.entity.Address;
import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.User;
//import com.example.galerie_artisanale.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByEmail(String email);

	List<User> findAll();

    List<User>  findByRole(String role);

   // List<User> findByRole_Users(String role);

}
