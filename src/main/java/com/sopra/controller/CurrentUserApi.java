package com.sopra.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.authority.Authority;
import com.sopra.core.authority.AuthorityService;
import com.sopra.core.history.History;
import com.sopra.core.rate.RateService;
import com.sopra.core.team.TeamService;
import com.sopra.core.theme.Theme;
import com.sopra.core.theme.ThemeService;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.core.utility.EncryptService;
import com.sopra.data.UserData;
import com.sopra.data.UserWithToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserApi {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private ThemeService themeService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private RateService rateService;

	@Autowired
	private EncryptService encryptService;

	@GetMapping(value = "/history")
	public ResponseEntity<?> getHistory(@AuthenticationPrincipal User currentUser) {

		User user = userService.findById(currentUser.getId()).get();

		List<UserHistory> list1 = new ArrayList<UserHistory>();
		List<History> histories=user.getHistories();
		Collections.sort(histories, (o1, o2) -> o2.getSeenAt().compareTo(o1.getSeenAt()));
		for (History h : histories) {

			Article a = articleService.findArticleBySlug(h.getArticleSlug());
			UserHistory userHistory = new UserHistory(a.getTitle(), a.getSlug(), h.getSeenAt().toString());
			list1.add(userHistory);

		}

		return ResponseEntity.ok(new HashMap<String, Object>() {
			{

				put("history", list1);
			}
		});
	}

	@GetMapping
	public ResponseEntity<?> currentUser(@AuthenticationPrincipal User currentUser,
			@RequestHeader(value = "Authorization") String authorization) {
		Optional<User> user = userService.findById(currentUser.getId());
		Authority authority = authorityService.findAuthorityByName("ADMIN");

		List<String> themes = new ArrayList<String>();
		for (Theme t : user.get().getThemes()) {
			themes.add(t.getName());
		}

		UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(), user.get().getBio(),
				user.get().getImage(), user.get().getAuthorities().contains(authority), themes,
				user.get().getTeam().getName());
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, authorization.split(" ")[1])));
	}

	@PostMapping(value = "/themes")
	public ResponseEntity<?> saveUserThemes(@AuthenticationPrincipal User user, @Valid @RequestBody NewThemes newThemes,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		Optional<User> Currentuser = userService.findById(user.getId());
		Currentuser.get().setThemes(new ArrayList<Theme>());

		for (String newTheme : newThemes.getThemes()) {
			Theme theme = new Theme();
			theme = themeService.findThemeByName(newTheme);
			Currentuser.get().getThemes().add(theme);
		}
		userService.save(Currentuser.get());

		return ResponseEntity.ok(newThemes);
	}

	@PutMapping
	public ResponseEntity updateProfile(@Valid @RequestBody UpdateUserParam updateUserParam,
			@AuthenticationPrincipal User currentUser, @RequestHeader("Authorization") String token,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}

		if (!updateUserParam.getPassword().equals(""))
			currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(),
					encryptService.encrypt(updateUserParam.getPassword()), updateUserParam.getPosition(),
					currentUser.getImage());
		else {
			if (updateUserParam.getImage().equals(""))
				currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(), currentUser.getPassword(),
						updateUserParam.getPosition(), currentUser.getImage());
			else
				currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(), currentUser.getPassword(),
						updateUserParam.getPosition(), updateUserParam.getImage());
		}
		if (!currentUser.getTeam().getName().equals(updateUserParam.getTeam())) {
			currentUser.setTeam(teamService.findTeamByName(updateUserParam.getTeam()));
		}
		userService.save(currentUser);
		Optional<User> user = userService.findById(currentUser.getId());
		Authority authority = authorityService.findAuthorityByName("ADMIN");
		UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(), user.get().getBio(),
				user.get().getImage(), user.get().getAuthorities().contains(authority));
		userData.setTeam(currentUser.getTeam().getName());
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
	}

	private void checkUniquenessOfUsernameAndEmail(User currentUser, UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		if (!"".equals(updateUserParam.getUsername())) {
			Optional<User> byUsername = userService.findByUsername(updateUserParam.getUsername());
			if (byUsername.isPresent() && !byUsername.get().equals(currentUser)) {
				bindingResult.rejectValue("username", "DUPLICATED", "username already exist");
			}
		}

		if (!"".equals(updateUserParam.getEmail())) {
			Optional<User> byEmail = userService.findByEmail(updateUserParam.getEmail());
			if (byEmail.isPresent() && !byEmail.get().equals(currentUser)) {
				bindingResult.rejectValue("email", "DUPLICATED", "email already exist");
			}
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	private Map<String, Object> userResponse(UserWithToken userWithToken) {
		return new HashMap<String, Object>() {
			{
				put("user", userWithToken);
			}
		};
	}
}

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserParam {
	private String position = "";
	private String email = "";
	private String image = "";
	private String password = "";
	private String username = "";
	private String team = "";

}

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class UserHistory {
	private String title = "";
	private String slug = "";
	private String seenAt = "";
}

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("newThemes")
class NewThemes {

	private List<String> themes = new ArrayList<String>();

}
