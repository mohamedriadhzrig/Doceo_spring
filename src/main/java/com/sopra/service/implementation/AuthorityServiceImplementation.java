package com.sopra.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.core.authority.Authority;
import com.sopra.core.authority.AuthorityService;
import com.sopra.repositories.AuthorityRepository;
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
