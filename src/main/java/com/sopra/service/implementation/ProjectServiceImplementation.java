package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.DAO.ProjectRepository;
import com.sopra.entities.Project;
import com.sopra.services.ProjectService;
@Transactional
@Service
public class ProjectServiceImplementation implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public Project save(Project project) {

		return projectRepository.save(project);
	}

	@Override
	public void delete(Project project) {
		projectRepository.delete(project);

	}

	@Override
	public void deleteById(Long id) {
		projectRepository.delete(id);

	}

	@Override
	public Project findProjectByName(String name) {
		// TODO Auto-generated method stub
		return projectRepository.findProjectByName(name);
	}

	@Override
	public Project findProjectById(Long id) {
		// TODO Auto-generated method stub
		return projectRepository.findOne(id);
	}

	@Override
	public List<String> findAll() {
		List<Project> projects = projectRepository.findAll();
		List<String> listProject = new ArrayList<String>();
		for (Project p : projects) {
			listProject.add(p.getName());
		}
		return listProject;
	}

	@Override
	public List<Project> findAllTeams() {
		// TODO Auto-generated method stub
		return projectRepository.findAll();
	}

}
