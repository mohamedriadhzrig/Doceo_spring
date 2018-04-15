package com.sopra.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.repositories.UserRepository;

@Transactional
@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public void save(User user) {
		userRepository.save(user);

	}

	@Override
	public void updatePaswword(String password, Long id) {
		userRepository.updatePassword(password, id);

	}

	@Override
	public Optional<User> findById(Long id) {

		return Optional.ofNullable(userRepository.findOne(id));
	}

	@Override
	public Optional<User> findByUsername(String username) {

		return Optional.ofNullable(userRepository.findUserByUsername(username));
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return Optional.ofNullable(userRepository.findUserByEmail(email));
	}

}
