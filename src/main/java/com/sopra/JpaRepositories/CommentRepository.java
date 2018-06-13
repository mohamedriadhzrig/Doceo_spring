package com.sopra.JpaRepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopra.core.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Comment findCommentById(Long id);
	
	Comment save(Comment comment);
}
