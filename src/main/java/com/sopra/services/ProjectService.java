package com.sopra.services;

import java.util.List;

import com.sopra.entities.Project;



public interface ProjectService {

	Project save(Project project);

	void delete(Project project);
	
	void deleteById(Long id);

	Project findProjectByName(String name);
	
	Project findProjectById(Long id);

	List<String> findAll();
	
	List<Project> findAllTeams();
	
}
