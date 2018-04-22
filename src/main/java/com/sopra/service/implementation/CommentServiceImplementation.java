package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.comment.Comment;
import com.sopra.core.comment.CommentService;
import com.sopra.data.CommentData;
import com.sopra.data.ProfileData;
import com.sopra.repositories.CommentRepository;




@Service
public class CommentServiceImplementation implements CommentService {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	ArticleService articleService;

	@Override
	public void save(Comment comment) {
		commentRepository.save(comment);

	}

	@Override
	public List<CommentData> findComments(Article article) {

		List<Comment> comments = article.getComments();
		List<CommentData> commentDatalist = new ArrayList<CommentData>();
		for (Comment c : comments) {
			CommentData commentData = new CommentData();
			commentData.setId(c.getId() + "");
			commentData.setCreatedAt(c.getCreatedAt());
			commentData.setUpdatedAt(c.getCreatedAt());
			commentData.setBody(c.getBody());
			commentData.setArticleId(article.getId() + "");
			ProfileData profileData = new ProfileData();
			profileData.setId(c.getUser().getId());
			profileData.setUsername(c.getUser().getUsername());
			profileData.setImage(c.getUser().getImage());
			profileData.setFollowing(false);
			profileData.setBio(c.getUser().getBio());
			commentData.setProfileData(profileData);
			commentDatalist.add(commentData);
		}

		return commentDatalist;
	}

}
