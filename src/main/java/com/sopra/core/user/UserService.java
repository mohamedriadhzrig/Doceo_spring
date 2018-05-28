package com.sopra.core.user;

import java.util.Optional;

import com.sopra.data.AdminUserDataList;

public interface UserService {
	User save(User user);

	AdminUserDataList findAll();

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	void updatePaswword(String password, Long id);

}
