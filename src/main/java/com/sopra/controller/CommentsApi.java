package com.sopra.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sopra.core.comment.Comment;
import com.sopra.core.comment.CommentService;
import com.sopra.core.user.User;
import com.sopra.data.CommentData;
import com.sopra.data.ProfileData;

import lombok.Getter;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping(path = "/articles/{slug}/comments")
public class CommentsApi {

	@Autowired
	ArticleService articleService;

	@Autowired
	CommentService commentService;

	@PostMapping
	public ResponseEntity<?> createComment(@PathVariable("slug") String slug, @AuthenticationPrincipal User user,
			@Valid @RequestBody NewCommentParam newCommentParam, BindingResult bindingResult) {
		Article article = articleService.findArticleBySlug(slug);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		Comment comment = new Comment();
		comment.setBody(newCommentParam.getBody());
		comment.setArticle(article);
		commentService.save(comment);
		comment.setUser(user);
		article.getComments().add(comment);
		articleService.save(article);
		
		ProfileData profileData = new ProfileData(user.getId(), user.getUsername(), user.getBio(), user.getImage(),
				false);
		CommentData commentData = new CommentData(0 + "", newCommentParam.getBody(), article.getId() + "", new Date(),
				new Date(), profileData);
		return ResponseEntity.status(201).body(commentResponse(commentData));
	}
	
	
	
	@GetMapping
    public ResponseEntity getComments(@PathVariable("slug") String slug,
                                      @AuthenticationPrincipal User user) {
        Article article = articleService.findArticleBySlug(slug);
        		     		
        		
        List<CommentData> comments = commentService.findComments(article);
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("comments", comments);
        }});
    }
	/*
	 * @GetMapping public ResponseEntity getComments(@PathVariable("slug") String
	 * slug,
	 * 
	 * @AuthenticationPrincipal User user) { Article article = findArticle(slug);
	 * List<CommentData> comments =
	 * commentQueryService.findByArticleId(article.getId(), user); return
	 * ResponseEntity.ok(new HashMap<String, Object>() {{ put("comments", comments);
	 * }}); }
	 * 
	 * @RequestMapping(path = "{id}", method = RequestMethod.DELETE) public
	 * ResponseEntity deleteComment(@PathVariable("slug") String slug,
	 * 
	 * @PathVariable("id") String commentId,
	 * 
	 * @AuthenticationPrincipal User user) { Article article = findArticle(slug);
	 * return commentRepository.findById(article.getId(), commentId).map(comment ->
	 * { if (!AuthorizationService.canWriteComment(user, article, comment)) { throw
	 * new NoAuthorizationException(); } commentRepository.remove(comment); return
	 * ResponseEntity.noContent().build();
	 * }).orElseThrow(ResourceNotFoundException::new); }
	 * 
	 */

	private Map<String, Object> commentResponse(CommentData commentData) {
		return new HashMap<String, Object>() {
			{
				put("comment", commentData);
			}
		};
	}
}

@Getter
@NoArgsConstructor

class NewCommentParam {
	@NotBlank(message = "can't be empty")
	private String body;
}
