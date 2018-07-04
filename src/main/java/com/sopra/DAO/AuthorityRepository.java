package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Authority findAuthorityByName(String name);
}
