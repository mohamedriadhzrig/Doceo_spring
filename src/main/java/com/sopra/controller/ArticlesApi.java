package com.sopra.controller;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.tag.Tag;
import com.sopra.core.tag.TagService;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.data.ArticleData;
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
	private UserService userService;

	@Autowired
	private TagService tagService;

	@GetMapping(value = "/{slug}")
	public ResponseEntity article(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {

		Article article = articleService.findArticleBySlug(slug);
		ProfileData profileData = new ProfileData();
		profileData.setBio(article.getUser().getBio());
		profileData.setUsername(article.getUser().getUsername());
		profileData.setImage(article.getUser().getImage());
		article.setSeen(article.getSeen());

		profileData.setFollowing(false);
		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getSeen(), false, 1, article.getCreatedAt(),
				article.getUpdatedAt(), article.getTags(), profileData);
		article.setSeen(article.getSeen() + 1);
		articleService.save(article);
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@PostMapping
	public ResponseEntity createArticle(@Valid @RequestBody NewArticleParam newArticleParam,
			BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}

		Article article = new Article(newArticleParam.getTitle(), newArticleParam.getDescription(),
				newArticleParam.getBody(), user);

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
				profileData.setFollowing(false);

				ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
						article.getDescription(), article.getBody(), article.getSeen(), false, 1,
						article.getCreatedAt(), article.getUpdatedAt(), article.getTags(), profileData);
				put("article", articleData);
			}
		});
	}

	@GetMapping
	public ResponseEntity getArticles(@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "20") int limit,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "favorited", required = false) String favoritedBy,
			@RequestParam(value = "author", required = false) String author, @AuthenticationPrincipal User user) {
		if (!(favoritedBy == null))
			return ResponseEntity.ok(articleService.findFavoriteArticles(favoritedBy));

		if (!(tag == null))
			return ResponseEntity.ok(articleService.findArticlesByTag(tag));

		if (!(author == null))
			return ResponseEntity.ok(articleService.findArticles(author));

		return null;
	}

	@PostMapping(value = "/{slug}/favorite")
	public ResponseEntity favoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
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
		profileData.setFollowing(false);

		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getSeen(), false, 1, article.getCreatedAt(),
				article.getUpdatedAt(), article.getTags(), profileData);
		return responseArticleData(articleData);
	}

	@DeleteMapping(path = "/{slug}/favorite")
	public ResponseEntity unfavoriteArticle(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		Article article = articleService.findArticleBySlug(slug);

		user.getFavoriteArticles().remove(article);
		userService.save(user);

		ProfileData profileData = new ProfileData();
		profileData.setBio(user.getBio());
		profileData.setUsername(user.getUsername());
		profileData.setImage(user.getImage());
		profileData.setId(user.getId());
		profileData.setFollowing(false);

		ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
				article.getDescription(), article.getBody(), article.getSeen(), false, 1, article.getCreatedAt(),
				article.getUpdatedAt(), article.getTags(), profileData);
		return responseArticleData(articleData);
	}

	/*
	 * @GetMapping(path = "feed") public ResponseEntity getFeed(@RequestParam(value
	 * = "offset", defaultValue = "0") int offset,
	 * 
	 * @RequestParam(value = "limit", defaultValue = "20") int limit,
	 * 
	 * @AuthenticationPrincipal User user) { return
	 * ResponseEntity.ok(articleQueryService.findUserFeed(user, new Page(offset,
	 * limit))); }
	 * 
	 * @GetMapping public ResponseEntity getArticles(@RequestParam(value = "offset",
	 * defaultValue = "0") int offset,
	 * 
	 * @RequestParam(value = "limit", defaultValue = "20") int limit,
	 * 
	 * @RequestParam(value = "tag", required = false) String tag,
	 * 
	 * @RequestParam(value = "favorited", required = false) String favoritedBy,
	 * 
	 * @RequestParam(value = "author", required = false) String author,
	 * 
	 * @AuthenticationPrincipal User user) { return
	 * ResponseEntity.ok(articleQueryService.findRecentArticles(tag, author,
	 * favoritedBy, new Page(offset, limit), user)); }
	 */
	private Map<String, Object> articleResponse(ArticleData articleData) {
		return new HashMap<String, Object>() {
			{
				put("article", articleData);
			}
		};
	}

	private ResponseEntity<HashMap<String, Object>> responseArticleData(final ArticleData articleData) {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("article", articleData);
			}
		});
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
	private List<String> tagList;
}