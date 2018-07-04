package com.sopra.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.entities.Article;
import com.sopra.entities.Project;
import com.sopra.entities.User;
import com.sopra.exception.InvalidRequestException;
import com.sopra.services.ArticleService;
import com.sopra.services.ProjectService;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/project")
public class ProjectApi {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping
	public ResponseEntity<?> getProjects() {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("projects", projectService.findAll());
			}
		});
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {

		for(Article a:projectService.findProjectById(id).getArticles())
		{
			a.setProject(null);
			articleService.save(a);
			
		}
		
		projectService.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	@PostMapping
	public ResponseEntity<?> createTeam(@AuthenticationPrincipal User user,
			@Valid @RequestBody NewTeamParam newProjectParam, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		projectService.save(new Project(newProjectParam.getName()));

		return ResponseEntity.noContent().build();
	}

}
@Getter
@NoArgsConstructor
class NewProjectParam {
	@NotBlank(message = "can't be empty")
	private String name;
}