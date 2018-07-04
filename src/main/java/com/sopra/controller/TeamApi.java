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
import com.sopra.entities.Team;
import com.sopra.entities.User;
import com.sopra.exception.InvalidRequestException;
import com.sopra.services.ArticleService;
import com.sopra.services.TeamService;
import com.sopra.services.UserService;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/team")
public class TeamApi {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ArticleService articleService;

	@GetMapping
	public ResponseEntity<?> getTeams() {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("teams", teamService.findAll());
			}
		});
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteTeam(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {

		for(User u:teamService.findTeamById(id).getUsers())
		{
			u.setTeam(null);
			userService.save(u);
			
		}
		for(Article a:teamService.findTeamById(id).getArticles())
		{
			a.setTeam(null);
			articleService.save(a);
			
		}
		
		teamService.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	@PostMapping
	public ResponseEntity<?> createTeam(@AuthenticationPrincipal User user,
			@Valid @RequestBody NewTeamParam newTeamParam, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		teamService.save(new Team(newTeamParam.getName()));

		return ResponseEntity.noContent().build();
	}

}

@Getter
@NoArgsConstructor
class NewTeamParam {
	@NotBlank(message = "can't be empty")
	private String name;
}
