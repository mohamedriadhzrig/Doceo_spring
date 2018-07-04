package com.sopra.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.DAO.AuthorityRepository;
import com.sopra.entities.Authority;
import com.sopra.services.AuthorityService;
@Service
public class AuthorityServiceImplementation implements AuthorityService {

	@Autowired
	AuthorityRepository authorityRepository;

	@Override
	public void save(Authority authority) {
		authorityRepository.save(authority);

	}

	@Override
	public Authority findAuthorityByName(String authority) {

		return authorityRepository.findAuthorityByName(authority);
	}

}
