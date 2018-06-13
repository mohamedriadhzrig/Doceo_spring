package com.sopra.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
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
import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.history.History;
import com.sopra.core.history.HistoryService;
import com.sopra.core.rate.Rate;
import com.sopra.core.rate.RateService;
import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.core.theme.Theme;
import com.sopra.core.theme.ThemeService;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.core.utility.SearchQueryBuilder;
import com.sopra.data.ArticleData;
import com.sopra.data.ArticleDataList;
import com.sopra.data.ProfileData;

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
	ThemeService themeService;

	@Autowired
	private SearchQueryBuilder searchQueryBuilder;

	@GetMapping(value = "/search/{text}")
	public ResponseEntity<?> searchArticle(@PathVariable String text, @AuthenticationPrincipal User currentUser) {

		User user = userService.findById(currentUser.getId()).get();
		List<Article> list = new ArrayList<Article>();

		list.addAll(searchQueryBuilder.getAll(text));
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

	}

	@GetMapping
	public ResponseEntity<?> getArticles(@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "favorited", required = false) String favoritedBy,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "admin", required = false) String admin,
			@RequestParam(value = "search", required = false) String search, @AuthenticationPrincipal User user) {
		User u = userService.findByUsername(user.getUsername()).get();
		if (!(favoritedBy == null))
			return ResponseEntity.ok(articleService.findFavoriteArticles(favoritedBy, u));
		if (!(tag == null))
			return ResponseEntity.ok(articleService.findArticlesByTag(tag, u));
		if (!(author == null)) {
			if (author == u.getUsername())
				return ResponseEntity.ok(articleService.findArticles(author, u));
			else
				return ResponseEntity.ok(articleService.findValidArticlesByUser(author, u));
		}
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
		Article article = new Article(newArticleParam.getTitle(), newArticleParam.getDescription(),
				newArticleParam.getBody(), user);
		article.setTheme(theme);
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
}

@Getter
@NoArgsConstructor
@JsonRootName("article")
class UpdateArticleParam {
	private String title = "";
	private String body = "";
	private String description = "";
}