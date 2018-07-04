package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Team;
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	
	
	Team findTeamByName(String name);
}
