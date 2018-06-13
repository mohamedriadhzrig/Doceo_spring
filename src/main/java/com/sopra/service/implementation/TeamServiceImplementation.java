package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.JpaRepositories.TeamRepository;
import com.sopra.core.team.Team;
import com.sopra.core.team.TeamService;

@Transactional
@Service
public class TeamServiceImplementation implements TeamService {

	@Autowired
	TeamRepository teamRepository;

	@Override
	public Team save(Team team) {

		return teamRepository.save(team);
	}

	@Override
	public void delete(Team team) {
		teamRepository.delete(team);
	}

	@Override
	public Team findTeamByName(String name) {

		return teamRepository.findTeamByName(name);
	}

	@Override
	public List<String> findAll() {

		List<Team> teams = teamRepository.findAll();
		List<String> listTeam = new ArrayList<String>();
		for (Team t : teams) {
			listTeam.add(t.getName());
		}
		return listTeam;
	}

	@Override
	public List<Team> findAllTeams() {

		return teamRepository.findAll();
	}

	@Override
	public Team findTeamById(Long id) {
		return teamRepository.findOne(id);
	}

	@Override
	public void deleteById(Long id) {
		teamRepository.delete(id);
		
	}

}
