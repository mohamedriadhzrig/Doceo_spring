package com.sopra.service.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.DAO.CommentRepository;
import com.sopra.data.CommentData;
import com.sopra.data.ProfileData;
import com.sopra.entities.Article;
import com.sopra.entities.Comment;
import com.sopra.services.ArticleService;
import com.sopra.services.CommentService;

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
		Collections.sort(comments, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
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
			profileData.setAdmin(false);
			profileData.setBio(c.getUser().getBio());
			commentData.setProfileData(profileData);
			commentDatalist.add(commentData);
		}

		return commentDatalist;
	}

	@Override
	public void remove(Comment comment) {
		
		commentRepository.delete(comment);
	}

	@Override
	public Comment findCommentById(Long id) {
		
		return commentRepository.findCommentById(id);
	}

	@Override
	public Comment saveAndReturn(Comment comment) {
		
		return commentRepository.save(comment);
	}

}
