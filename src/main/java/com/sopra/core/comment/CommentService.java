package com.sopra.core.comment;

import java.util.List;
import java.util.Optional;

import com.sopra.core.article.Article;
import com.sopra.data.CommentData;

public interface  CommentService {
    void save(Comment comment);
    
    List<CommentData> findComments(Article article);
    
    void remove(Comment comment);
    
    Comment findCommentById(Long id);
    
    Comment saveAndReturn(Comment comment);

    
}
