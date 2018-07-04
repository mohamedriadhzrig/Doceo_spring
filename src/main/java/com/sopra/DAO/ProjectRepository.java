package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopra.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	Project findProjectByName(String name);

}
