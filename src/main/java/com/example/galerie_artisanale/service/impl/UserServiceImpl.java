package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.entity.UserRole;
import com.example.galerie_artisanale.repository.PasswordResetTokenRepository;
import com.example.galerie_artisanale.repository.UserRepository;
import com.example.galerie_artisanale.repository.UserRoleRepository;
import com.example.galerie_artisanale.security.PasswordResetToken;
import com.example.galerie_artisanale.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;


	@Transactional
	@Override
	public User createUser(User user, String userRole){
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if(localUser != null) {
			LOG.info("User with username {} already exist. Nothing will be done. ",user.getUsername());
		} else {

			UserRole byRole = userRoleRepository.findByRole(userRole);
			if(byRole == null){
				byRole = new UserRole();
				byRole.setRole(userRole);

				byRole = userRoleRepository.save(byRole);
			}

			user.setRole(byRole);
			
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}


	@Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token,user);
		passwordResetTokenRepository.save(myToken);
	}


	@Override
	public User save(User user)  {
		return userRepository.save(user);
	}

	@Override
	public User findById(Long id) {

		Optional<User> byId= userRepository.findById(id);
		return byId.orElse(null);
	}

	@Override
	public List<User> findAll() {
		return  userRepository.findAll();
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	

}
