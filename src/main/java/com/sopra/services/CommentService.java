package com.sopra.services;

import java.util.List;
import java.util.Optional;

import com.sopra.data.CommentData;
import com.sopra.entities.Article;
import com.sopra.entities.Comment;

public interface  CommentService {
    void save(Comment comment);
    
    List<CommentData> findComments(Article article);
    
    void remove(Comment comment);
    
    Comment findCommentById(Long id);
    
    Comment saveAndReturn(Comment comment);

    
}
