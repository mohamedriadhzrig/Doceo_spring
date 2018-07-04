package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.DAO.AuthorityRepository;
import com.sopra.DAO.UserRepository;
import com.sopra.data.AdminUserDataList;
import com.sopra.data.UserDetails;
import com.sopra.entities.Authority;
import com.sopra.entities.User;
import com.sopra.services.UserService;

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
			if(!(x.getTeam()==null))
			u.setTeam(x.getTeam().getName());
			else
			u.setTeam("undefined");
			u.setArticleCount((long) x.getArticles().size());

			u.setCommentCount((long) x.getComments().size());

			u.setAdmin(x.getAuthorities().contains(authority));

			usersDetails.add(u);

		}

		AdminUserDataList adminUserDataList = new AdminUserDataList(usersDetails);

		return adminUserDataList;
	}

}
