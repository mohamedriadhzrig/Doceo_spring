package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sopra.core.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
