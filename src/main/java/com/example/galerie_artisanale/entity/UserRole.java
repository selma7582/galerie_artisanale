package com.example.galerie_artisanale.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="user_roles")
public class UserRole implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @OneToMany(mappedBy = "role", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users ;


    @Column(name="role")
    private String role;



}
