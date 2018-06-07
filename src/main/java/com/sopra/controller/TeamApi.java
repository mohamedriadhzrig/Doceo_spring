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

import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.team.Team;
import com.sopra.core.team.TeamService;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/team")
public class TeamApi {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;

	@GetMapping
	public ResponseEntity getTeams() {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("teams", teamService.findAll());
			}
		});
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteTeam(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {

		for(User u:teamService.findTeamById(id).getUsers())
		{
			u.setTeam(null);
			userService.save(u);
			
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
