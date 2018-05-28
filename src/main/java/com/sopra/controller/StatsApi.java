package com.sopra.controller;

import java.util.ArrayList;
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
import com.sopra.core.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping(path = "/stats")
public class StatsApi {

	@Autowired
	private TagService tagService;

	@Autowired
	private TeamService teamService;

	@GetMapping(value = "/tags")
	public ResponseEntity statsTag(@AuthenticationPrincipal User user) {

		List<Tag> tagList = tagService.findAllTagsOrderByName();
		List<TagStats> stats = new ArrayList<>();
		for (Tag t : tagList) {
			TagStats tagStats = new TagStats(t.getName(), t.getArticles().size());
			stats.add(tagStats);

		}
		/*
		 * Article article = articleService.findArticleBySlug(slug); ProfileData
		 * profileData = new ProfileData();
		 * profileData.setBio(article.getUser().getBio() + "");
		 * profileData.setUsername(article.getUser().getUsername());
		 * profileData.setImage(article.getUser().getImage());
		 * article.setSeen(article.getSeen()); User u =
		 * userService.findByUsername(user.getUsername()).get();
		 * profileData.setAdmin(false);
		 * 
		 * Double rating; rating = rateService.findArticleRatings(slug); if (rating ==
		 * null) { rating = 0.0; } DecimalFormat oneDigit = new DecimalFormat("#,##0.0",
		 * new DecimalFormatSymbols(Locale.ENGLISH));
		 * 
		 * ArticleData articleData = new ArticleData(article.getId(), article.getSlug(),
		 * article.getTitle(), article.getDescription(), article.getBody(),
		 * article.getFileType(), article.getSeen(), article.getLikedBy().contains(u),
		 * article.getLikedBy().size(), article.getCreatedAt(), article.getUpdatedAt(),
		 * article.getTags(), profileData, Double.valueOf(oneDigit.format(rating))); if
		 * (!(article.getUser().getUsername().equals(user.getUsername()))) {
		 * article.setSeen(article.getSeen() + 1); articleService.save(article); }
		 */
		return ResponseEntity.ok(stats);
	}

	@GetMapping(value = "/teams")
	public ResponseEntity statsTeam(@AuthenticationPrincipal User user) {

		List<Team> teamList = teamService.findAllTeams();
		List<TagStats> stats = new ArrayList<>();
		for (Team t : teamList) {
			TagStats tagStats = new TagStats(t.getName(), t.getUsers().size());
			stats.add(tagStats);

		}
		return ResponseEntity.ok(stats);
	}

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class TagStats {

	private String name = "";
	private int used = 0;

}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class TeamStats {

	private String name = "";
	private int used = 0;

}
