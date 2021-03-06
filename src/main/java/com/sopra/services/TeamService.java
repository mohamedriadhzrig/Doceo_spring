package com.sopra.services;

import java.util.List;

import com.sopra.entities.Team;

public interface TeamService {

	Team save(Team team);

	void delete(Team team);
	
	void deleteById(Long id);

	Team findTeamByName(String name);
	
	Team findTeamById(Long id);

	List<String> findAll();
	
	List<Team> findAllTeams();
	
	

}
