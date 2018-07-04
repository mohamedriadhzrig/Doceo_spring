package com.sopra.services;

import java.util.Optional;

import com.sopra.data.AdminUserDataList;
import com.sopra.entities.User;

public interface UserService {
	User save(User user);

	AdminUserDataList findAll();

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	void updatePaswword(String password, Long id);

}
