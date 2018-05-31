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

import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.core.team.Team;
import com.sopra.core.team.TeamService;
import com.sopra.core.theme.Theme;
import com.sopra.core.theme.ThemeService;
import com.sopra.core.user.User;

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
	private TeamService teamService;

	@GetMapping(value = "/tags")
	public ResponseEntity statsTag(@AuthenticationPrincipal User user) {

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
	public ResponseEntity statsTeam(@AuthenticationPrincipal User user) {

		List<Team> teamList = teamService.findAllTeams();
		List<TeamStats> stats = new ArrayList<>();
		for (Team t : teamList) {
			TeamStats tagStats = new TeamStats(t.getId(), t.getName(), t.getUsers().size());
			stats.add(tagStats);

		}
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("teams", stats);
			}
		});
	}

	@GetMapping(value = "/themes")
	public ResponseEntity statsThemes(@AuthenticationPrincipal User user) {

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
class ThemeStats {
	private Long id;
	private String name = "";
	private int articles = 0;

}
