package com.sopra.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.entities.Project;
import com.sopra.entities.Tag;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;
import com.sopra.services.ProjectService;
import com.sopra.services.TagService;
import com.sopra.services.TeamService;
import com.sopra.services.ThemeService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping(path = "/stats")
public class StatsApi {

	@Autowired
	private ThemeService themeService;

	@Autowired
	private TagService tagService;
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private TeamService teamService;

	@GetMapping(value = "/tags")
	public ResponseEntity<?> statsTag(@AuthenticationPrincipal User user) {

		List<Tag> tagList = tagService.findAllTagsOrderByName();
		List<TagStats> stats = new ArrayList<>();
		for (Tag t : tagList) {
			TagStats tagStats = new TagStats(t.getId(), t.getName(), t.getArticles().size());
			stats.add(tagStats);

		}
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("tags", stats);
			}
		});
	}

	@GetMapping(value = "/teams")
	public ResponseEntity<?> statsTeam(@AuthenticationPrincipal User user) {

		List<Team> teamList = teamService.findAllTeams();
		List<TeamStats> stats = new ArrayList<>();
		for (Team t : teamList) {
			TeamStats teamStats = new TeamStats(t.getId(), t.getName(), t.getUsers().size());
			stats.add(teamStats);

		}
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("teams", stats);
			}
		});
	}
	@GetMapping(value = "/projects")
	public ResponseEntity<?> statsProjects(@AuthenticationPrincipal User user) {

		List<Project> projectList = projectService.findAllTeams();
		List<ProjectStats> stats = new ArrayList<>();
		for (Project t : projectList) {
			ProjectStats projectStats = new ProjectStats(t.getId(), t.getName(), t.getArticles().size());
			stats.add(projectStats);

		}
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("projects", stats);
			}
		});
	}

	@GetMapping(value = "/themes")
	public ResponseEntity<?> statsThemes(@AuthenticationPrincipal User user) {

		List<Theme> themeList = themeService.findAll();
		List<ThemeStats> stats = new ArrayList<>();
		for (Theme t : themeList) {
			ThemeStats themeStats = new ThemeStats(t.getId(), t.getName(), t.getArticles().size());
			stats.add(themeStats);

		}
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("themes", stats);
			}
		});
	}

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class TagStats {
	private Long id;
	private String name = "";
	private int used = 0;

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class TeamStats {
	private Long id;
	private String name = "";
	private int members = 0;

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class ProjectStats {
	private Long id;
	private String name = "";
	private int articles = 0;

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class ThemeStats {
	private Long id;
	private String name = "";
	private int articles = 0;

}
