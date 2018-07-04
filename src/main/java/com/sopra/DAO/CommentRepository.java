package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopra.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	Comment findCommentById(Long id);
	
	Comment save(Comment comment);
}
