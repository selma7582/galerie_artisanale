package com.example.galerie_artisanale.service;

import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.security.PasswordResetToken;

import java.util.List;
import java.util.Optional;


public interface UserService {

	PasswordResetToken getPasswordResetToken(final String token);

	User createUser(User user, String userRole)throws Exception;

	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User save(User user);
	
	User findById(Long id);

	List<User> findAll();

	void createPasswordResetTokenForUser(final User user, final String token);
	
}
