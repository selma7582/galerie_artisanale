package com.example.galerie_artisanale.repository;

import com.example.galerie_artisanale.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {


    UserRole findByRole(String role);
}

