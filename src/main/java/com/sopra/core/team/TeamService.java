package com.sopra.core.team;

import java.util.List;

public interface TeamService {

	Team save(Team team);

	void delete(Team team);

	Team findTeamByName(String name);

	List<String> findAll();
	
	List<Team> findAllTeams();
	
	

}
