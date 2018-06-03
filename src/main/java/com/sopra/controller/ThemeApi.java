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
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.team.Team;
import com.sopra.core.team.TeamService;
import com.sopra.core.theme.Theme;
import com.sopra.core.theme.ThemeService;
import com.sopra.core.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/theme")
public class ThemeApi {
	
	@Autowired
	private ThemeService themeService;
	
	@Autowired
	private ArticleService articleService;

	@GetMapping
	public ResponseEntity getTeams() {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("themes", themeService.findAllTheme());
			}
		});
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteTheme(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {

		for(Article a:themeService.findThemeById(id).getArticles())
		{
			
			a.setTheme(null);
			articleService.save(a);
		}
		
		themeService.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	
	@PostMapping
	public ResponseEntity<?> createTheme(@AuthenticationPrincipal User user,
			@Valid @RequestBody NewTeamParam newThemeParam, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		themeService.save(new Theme(newThemeParam.getName()));

		return ResponseEntity.noContent().build();
	}

}

@Getter
@NoArgsConstructor
class NewThemeParam {
	@NotBlank(message = "can't be empty")
	private String name;
}