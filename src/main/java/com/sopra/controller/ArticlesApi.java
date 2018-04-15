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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.article.Tag;
import com.sopra.core.user.User;
import com.sopra.data.ArticleData;
import com.sopra.data.ProfileData;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/articles")
public class ArticlesApi {

	@Autowired
	private ArticleService articleService;

	@GetMapping(value = "/{slug}")
	public ResponseEntity<?> article(@PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
		ProfileData profileData = new ProfileData();
		profileData.setBio(user.getBio());
		profileData.setUsername(user.getUsername());
		profileData.setImage(user.getImage());
		profileData.setId("");
		profileData.setFollowing(false);
		Optional<Article> article = articleService.findBySlug(slug, user);
		ArticleData articleData = new ArticleData(article.get().getId(), article.get().getSlug(),
				article.get().getTitle(), article.get().getDescription(), article.get().getBody(), false, 1,
				article.get().getCreatedAt(), article.get().getUpdatedAt(), article.get().getTags(), profileData);
		return ResponseEntity.ok(articleResponse(articleData));
	}

	@PostMapping
	public ResponseEntity createArticle(@Valid @RequestBody NewArticleParam newArticleParam,
			BindingResult bindingResult, @AuthenticationPrincipal User user) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}

		Article article = new Article(newArticleParam.getTitle(), newArticleParam.getDescription(),
				newArticleParam.getBody(), newArticleParam.getTagList(), user);
		articleService.save(article);
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				ProfileData profileData = new ProfileData();
				profileData.setBio(user.getBio());
				profileData.setUsername(user.getUsername());
				profileData.setImage(user.getImage());
				profileData.setId("");
				profileData.setFollowing(false);

				ArticleData articleData = new ArticleData(article.getId(), article.getSlug(), article.getTitle(),
						article.getDescription(), article.getBody(), false, 1, article.getCreatedAt(),
						article.getUpdatedAt(), article.getTags(), profileData);
				put("article", articleData);
			}
		});
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
}

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
	private List<Tag> tagList;
}