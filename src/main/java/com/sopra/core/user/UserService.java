package com.sopra.core.user;

import java.util.Optional;

public interface UserService {
	void save(User user);

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	void updatePaswword(String password, Long id);

}
