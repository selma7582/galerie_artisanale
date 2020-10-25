package com.example.galerie_artisanale.entity;

import com.example.galerie_artisanale.security.Authority;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@NotNull
    @Column(name = "userid")
    private Long id_user;

    @Column(name = "username")
    // @NotNull
    //@Size(min = 1, message = "Username  obligatoire")
    private String username;

    @Column(name = "password")
    @NotNull
    //@Size(min = 1, message = "password  obligatoire")
    private String password;

    @Column(name = "firstname")
    private String firstName;


    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    private String email;

    // @Column(name = "")
/*
    private Date naissance;
*/

    @Column(name = "tel")
    private String tel;

    private int enabled;


    @ManyToOne
    @JoinColumn(name="role_id")
    private UserRole role;

    public String getUsername() {
        return email;
    }

    public void setUsername(String email) {
        this.username = email;
    }

  /*  @OneToMany(cascade = CascadeType.ALL , mappedBy = "user")
    private List<Ordered> orderedList;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "user")
    private List<Address>  addressList;*/

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new Authority(getRole().getRole()));
        return authorities;
    }


}


