package com.sopra.core.authority;

public interface AuthorityService {
	void save(Authority authority);
	
	Authority findAuthorityByName(String authority);
}
