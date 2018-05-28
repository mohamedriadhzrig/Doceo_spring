package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopra.core.team.Team;
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Team save(Team team);
	
	Team findTeamByName(String name);
}
