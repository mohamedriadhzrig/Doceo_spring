package com.sopra.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sopra.DAO.TagRepository;
import com.sopra.data.ArticleData;
import com.sopra.data.ArticleDataList;
import com.sopra.data.ProfileData;
import com.sopra.entities.Article;
import com.sopra.entities.History;
import com.sopra.entities.Project;
import com.sopra.entities.Rate;
import com.sopra.entities.Tag;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;
import com.sopra.exception.InvalidRequestException;
import com.sopra.services.ArticleService;
import com.sopra.services.HistoryService;
import com.sopra.services.ProjectService;
import com.sopra.services.RateService;
import com.sopra.services.TagService;
import com.sopra.services.TeamService;
import com.sopra.services.ThemeService;
import com.sopra.services.UserService;
import com.sopra.utility.MailService;
import com.sopra.utility.SearchQueryBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@RestController
@RequestMapping(path = "/articles")
public class ArticlesApi {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private RateService rateService;

	@Autowired
	private UserService userService;

	@Autowired
	private TagService tagService;

	@Autowired
	private ThemeService themeService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MailService mailService;

	@Autowired
	private SearchQueryBuilder searchQueryBuilder;

	@GetMapping(value = "/advancedsearch")
	public ResponseEntity<?> searchArticles(@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "project", required = false) String project,
			@RequestParam(value = "theme", required = false) String theme,
			@RequestParam(value = "team", required = false) String team,
			@RequestParam(value = "search", required = false) String search, @AuthenticationPrincipal User user) {
		User u = userService.findByUsername(user.getUsername()).get();
		Tag tagSearch=new Tag();
		Theme themeSearch=new Theme();
		Project projectSearch=new Project();
		String textSearch ;
		Team teamSearch=new Team();
		
		if (tag == null || tag=="")
			tagSearch=null;
		else
		{
			Optional<Tag> existingTag = tagService.findTagByName(tag);
			tagSearch=existingTag.get();
		}
		
		if (theme == null || theme=="")
			themeSearch=null;
		else
		{
			themeSearch=themeService.findThemeByName(theme);
		}
		
		if (project == null || project=="")
			projectSearch=null;
		else
		{
			projectSearch=projectService.findProjectByName(project);
		}
		
		if (team == null || team=="")
			teamSearch=null;
		else
		{
			teamSearch=teamService.findTeamByName(team);
		}
		if (search == null || search=="")
			textSearch=null;
		else
		{
			textSearch="%"+search+"%";
		}
		return ResponseEntity.ok(articleService.findArticlesBy(textSearch, projectSearch, teamSearch, themeSearch, tagSearch, u));
	}
	
	@GetMapping
	public ResponseEntity<?> getArticles(@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "favorited", required = false) String favoritedBy,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "admin", required = false) String admin,
			@RequestParam(value = "project", required = false) String project,
			@RequestParam(value = "theme", required = false) String theme,
			@RequestParam(value = "team", required = false) String team,
			@RequestParam(value = "search", required = false) String search, @AuthenticationPrincipal User user) {
		User u = userService.findByUsername(user.getUsername()).get();
		
		if (!(favoritedBy == null))
			return ResponseEntity.ok(articleService.findFavoriteArticles(favoritedBy, u));
		if (!(tag == null))
			if (!(theme == null))
				return ResponseEntity.ok(articleService.findArticlesByTagAndTheme(tag, theme, user));
			else
				return ResponseEntity.ok(articleService.findArticlesByTag(tag, u));
		if (!(author == null)) {
			if (author == u.getUsername())
				return ResponseEntity.ok(articleService.findArticles(author, u));
			else
				return ResponseEntity.ok(articleService.findValidArticlesByUser(author, u));
		}
		if (!(theme == null))
			return ResponseEntity.ok(articleService.findArticlesByTheme(theme, u));
		if (!(admin == null)) {
			if (admin.equals("articles"))
				return ResponseEntity.ok(articleService.findAllInvalide());
			if (admin.equals("users"))
				return ResponseEntity.ok(userService.findAll());
		}
		if (!(search == null))
			return this.searchArticle(search, user);
		return ResponseEntity.ok(articleService.findAllValide(u));
	}
	
	@GetMapping(value = "/search/{text}")
	public ResponseEntity<?> searchArticle(@PathVariable String text, @AuthenticationPrincipal User currentUser) {

		User user = userService.findById(currentUser.getId()).get();
		List<Article> list = new ArrayList<Article>();

		list.addAll(searchQueryBuilder.getAll(text));
		Collections.sort(list, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article b : list) {
			Article a = articleService.findArticleBySlug(b.getSlug());
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(userService.findById(a.getUser().getId()).get().getUsername() == user.getUsername()
					|| a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return ResponseEntity.ok(articleDataList);
	}

	@GetMapping(value = "/titles/{text}")
	public ResponseEntity<?> getTitles(@PathVariable String text, @AuthenticationPrincipal User currentUser) {

		User user = userService.findById(currentUser.getId()).get();
		List<Article> list = new ArrayList<Article>();

		list.addAll(searchQueryBuilder.getTitles(text));
		List<ArticleData> list1 = new ArrayList<ArticleData>();
		for (Article b : list) {
			Article a = articleService.findArticleBySlug(b.getSlug());
			ArticleData articleData = new ArticleData();
			articleData.setSeen(a.getSeen());
			articleData.setId(a.getId());
			articleData.setBody(a.getBody());
			articleData.setCreatedAt(a.getCreatedAt());
			articleData.setDescription(a.getDescription());
			articleData.setSlug(a.getSlug());
			articleData.setTitle(a.getTitle());
			articleData.setTagList(a.getTags());
			articleData.setUpdatedAt(a.getUpdatedAt());
			articleData.setFavorited(userService.findById(a.getUser().getId()).get().getUsername() == user.getUsername()
					|| a.getLikedBy().contains(user));
			articleData.setFavoritesCount(a.getLikedBy().size());
			Double rating;
			rating = rateService.findArticleRatings(a.getSlug());
			if (rating == null) {
				rating = 0.0;
			}
			articleData.setRating(rating);
			ProfileData profileData = new ProfileData();
			profileData.setId(a.getUser().getId());
			profileData.setUsername(a.getUser().getUsername());
			profileData.setImage(a.getUser().getImage());
			profileData.setAdmin(false);
			profileData.setBio(a.getUser().getBio());
			articleData.setProfileData(profileData);
			list1.add(articleData);

		}

		ArticleDataList articleDataList = new ArticleDataList(list1, list1.size());
		return ResponseEntity.ok(articleDataList);
	}

	@GetMapping(value = "/tags/{text}")
	public ResponseEntity<?> getTagss(@PathVariable String text, @AuthenticationPrincipal User currentUser) {

		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("tags", tagService.findSuggestion(text));
			}
		});
	}

	@GetMapping(value = "/{slug}")
	public ResponseEntity<?> article(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {

		Article article = articleService.findArticleBySlug(slug);
		History history = new History(user, article.getSlug());
		historyService.save(history);
		ProfileData profileData = new ProfileData();
		profileData.setBio(article.getUser().getBio() + "");
		profileData.setUsername(article.getUser().getUsername());
		profileData.setImage(article.getUser().getImage());
		article.setSeen(article.getSeen());
		User u = userService.findByUsername(user.getUsername()).get();
		profileData.setAdmin(false);

		Double rating;
		rating = rateService.findArticleRatings(slug);
		if (rating == null) {
			rating = 0.0;
		}
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.ENGLISH));

		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(),
				article.getLikedBy().contains(u), article.getLikedBy().size(), article.getCreatedAt(),
				article.getUpdatedAt(), article.getTags(), profileData, Double.valueOf(oneDigit.format(rating)));
		if (!(article.getUser().getUsername().equals(user.getUsername()))) {
			article.setSeen(article.getSeen() + 1);
			articleService.save(article);
		}
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@GetMapping(path = "/feed")
	public ResponseEntity<?> getFeed(@AuthenticationPrincipal User user) {

		return ResponseEntity.ok(articleService.getFeed(userService.findById(user.getId()).get()));
	}

	@DeleteMapping(value = "/{slug}")
	public ResponseEntity<?> deleteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);
		articleService.remove(article);
		mailService.sendMail(article.getUser().getEmail(), "Deleted article", "Your article intitled : "+article.getTitle()+" has been deleted from the platform");
		return ResponseEntity.noContent().build();

	}

	@PutMapping(value = "/{slug}")
	public ResponseEntity<?> updateArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user,
			@Valid @RequestBody UpdateArticleParam updateArticleParam) {
		Article article = articleService.findArticleBySlug(slug);
		article.setTitle(updateArticleParam.getTitle());
		article.setDescription(updateArticleParam.getDescription());
		article.setBody(updateArticleParam.getBody());
		articleService.save(article);
		User u = userService.findByUsername(user.getUsername()).get();
		ProfileData profileData = new ProfileData();
		profileData.setBio(article.getUser().getBio());
		profileData.setUsername(article.getUser().getUsername());
		profileData.setImage(article.getUser().getImage());
		Double rating = rateService.findArticleRatings(slug);
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(),
				article.getLikedBy().contains(u), article.getLikedBy().size(), article.getCreatedAt(),
				article.getUpdatedAt(), article.getTags(), profileData, Double.valueOf(oneDigit.format(rating)));
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@PutMapping(value = "/{slug}/validate")
	public void validateArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);
		
		article.setStatut("valide");
		article.setValidatedAt(new Date());
		articleService.save(article);
		Team team=article.getTeam();
		for(User u:team.getUsers())
		{
			mailService.sendMail(u.getEmail(), "New article added: "+article.getTitle(), "The description of this article is : "+article.getDescription());
		}
		mailService.sendMail(article.getUser().getEmail(),"Article Validated By "+user.getUsername(),"Your article titled: "+article.getTitle()+" was validated and added to the platform ");

	}

	

	@GetMapping(value = "/invalide/admin")
	public ResponseEntity<?> invalideArticle(@AuthenticationPrincipal User user) {

		return ResponseEntity.ok(articleService.findAllInvalide());
	}

	@PostMapping(value = "/{slug}/favorite")
	public ResponseEntity<?> favoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);
		User u = new User();
		u = userService.findByUsername(user.getUsername()).get();
		u.getFavoriteArticles().add(article);
		userService.save(u);

		ProfileData profileData = new ProfileData();
		profileData.setBio(u.getBio());
		profileData.setUsername(u.getUsername());
		profileData.setImage(u.getImage());
		profileData.setId(u.getId());
		profileData.setAdmin(false);
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		Double rating = rateService.findArticleRatings(article.getSlug());
		if (rating == null) {
			rating = 0.0;
		}
		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(), false, 1,
				article.getCreatedAt(), article.getUpdatedAt(), article.getTags(), profileData,
				Double.valueOf(oneDigit.format(rating)));
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@DeleteMapping(path = "/{slug}/favorite")
	public ResponseEntity<?> unfavoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);
		User u = new User();
		u = userService.findByUsername(user.getUsername()).get();
		u.getFavoriteArticles().remove(article);
		userService.save(user);

		ProfileData profileData = new ProfileData();
		profileData.setBio(user.getBio());
		profileData.setUsername(user.getUsername());
		profileData.setImage(user.getImage());
		profileData.setId(user.getId());
		profileData.setAdmin(false);
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		Double rating = rateService.findArticleRatings(article.getSlug());
		if (rating == null) {
			rating = 0.0;
		}
		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(), false, 1,
				article.getCreatedAt(), article.getUpdatedAt(), article.getTags(), profileData,
				Double.valueOf(oneDigit.format(rating)));
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@PostMapping(value = "/{slug}/rate/{value}")
	public ResponseEntity<?> favoriteArticle(@PathVariable("slug") String slug, @PathVariable("value") Double value,
			@AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);
		User u = new User();
		u = userService.findByUsername(user.getUsername()).get();
		Optional<Rate> rate = rateService.findRateByUsernameInArticle(user.getUsername(), slug);
		if (rate.isPresent()) {
			rate.get().setValue(value);
			rateService.save(rate.get());
		} else {
			Rate r = new Rate();
			r.setArticle(article);
			r.setUser(u);
			r.setValue(value);
			rateService.save(r);
			article.getRatings().add(r);
			articleService.save(article);
		}

		ProfileData profileData = new ProfileData();
		profileData.setBio(u.getBio());
		profileData.setUsername(u.getUsername());
		profileData.setImage(u.getImage());
		profileData.setId(u.getId());
		profileData.setAdmin(false);
		DecimalFormat oneDigit = new DecimalFormat("#,##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		Double rating = rateService.findArticleRatings(article.getSlug());
		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(), false, 1,
				article.getCreatedAt(), article.getUpdatedAt(), article.getTags(), profileData,
				Double.valueOf(oneDigit.format(rating)));
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@PostMapping
	public ResponseEntity<?> createArticle(@Valid @RequestBody NewArticleParam newArticleParam,
			BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		Theme theme = themeService.findThemeByName(newArticleParam.getTheme());
		Team team = teamService.findTeamByName(newArticleParam.getTeam());
		Project project = projectService.findProjectByName(newArticleParam.getProject());
		Article article = new Article(newArticleParam.getTitle(), newArticleParam.getDescription(),
				newArticleParam.getBody(), user);
		article.setTheme(theme);
		article.setTeam(team);
		article.setProject(project);
		article.setFileType(newArticleParam.getFileType());
		article.setStatut("invalide");
		for (String t : newArticleParam.getTagList()) {
			Optional<Tag> existingTag = tagService.findTagByName(t);
			if (!existingTag.isPresent()) {
				Tag tag = new Tag();
				tag.setName(t);
				article.getTags().add(tag);
				tag.getArticles().add(article);
			} else {
				article.getTags().add(existingTag.get());
				existingTag.get().getArticles().add(article);
			}

		}

		articleService.save(article);

		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				ProfileData profileData = new ProfileData();
				profileData.setBio(user.getBio());
				profileData.setUsername(user.getUsername());
				profileData.setImage(user.getImage());
				profileData.setId(user.getId());
				profileData.setAdmin(false);

				ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
						article.getDescription(), article.getBody(), article.getFileType(), article.getSeen(), false, 1,
						article.getCreatedAt(), article.getUpdatedAt(), article.getTags(), profileData, 0.0);
				put("article", articleData);
			}
		});
	}

	private Map<String, Object> articleResponse(ArticleData articleData) {
		return new HashMap<String, Object>() {
			{
				put("article", articleData);
			}
		};
	}
}

@ToString
@Getter
@JsonRootName("article")
@NoArgsConstructor
class NewArticleParam {
	@NotBlank(message = "can't be empty")
	private String title;
	@NotBlank(message = "can't be empty")
	private String description;
	@NotBlank(message = "can't be empty")
	private String body;
	@NotBlank(message = "can't be empty")
	private String fileType;
	private List<String> tagList;
	@NotBlank(message = "can't be empty")
	private String theme;
	@NotBlank(message = "can't be empty")
	private String team;
	@NotBlank(message = "can't be empty")
	private String project;
}

@Getter
@NoArgsConstructor
@JsonRootName("article")
class UpdateArticleParam {
	private String title = "";
	private String body = "";
	private String description = "";
}