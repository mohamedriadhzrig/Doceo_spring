package com.sopra.JpaRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.authority.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findAuthorityByName(String name);
}
