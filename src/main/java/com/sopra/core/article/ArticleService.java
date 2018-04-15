package com.sopra.core.article;

import java.util.Optional;

import com.sopra.core.user.User;

public interface ArticleService {

	void save(Article article);

	Optional<Article> findById(Long id);

	Optional<Article> findBySlug(String slug, User user);

	void remove(Article article);
}
