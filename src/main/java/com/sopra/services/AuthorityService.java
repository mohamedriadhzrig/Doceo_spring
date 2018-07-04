package com.sopra.services;

import com.sopra.entities.Authority;

public interface AuthorityService {
	void save(Authority authority);
	
	Authority findAuthorityByName(String authority);
}
