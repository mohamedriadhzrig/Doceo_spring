package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.core.authority.Authority;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.data.AdminUserDataList;
import com.sopra.data.UserDetails;
import com.sopra.repositories.AuthorityRepository;
import com.sopra.repositories.UserRepository;

@Transactional
@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthorityRepository authorityRepository;

	@Override
	public User save(User user) {
		return  userRepository.save(user);
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

	@Override
	public AdminUserDataList findAll() {

		List<User> users = userRepository.findAll();

		List<UserDetails> usersDetails = new ArrayList<>();

		Authority authority = authorityRepository.findAuthorityByName("ADMIN");

		for (User x : users) {

			UserDetails u = new UserDetails();
			u.setUsername(x.getUsername());
			u.setEmail(x.getEmail());
			u.setTeam(x.getTeam().getName());
			u.setArticleCount((long) x.getArticles().size());

			u.setCommentCount((long) x.getComments().size());

			u.setAdmin(x.getAuthorities().contains(authority));

			usersDetails.add(u);

		}

		AdminUserDataList adminUserDataList = new AdminUserDataList(usersDetails);

		return adminUserDataList;
	}

}
