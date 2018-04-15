package com.sopra.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.core.article.Article;
import com.sopra.core.article.ArticleService;
import com.sopra.core.user.User;
import com.sopra.repositories.ArticleRepository;

@Service
public class ArticleServiceImplementation implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Override
	public void save(Article article) {

		articleRepository.save(article);

	}

	@Override
	public Optional<Article> findById(Long id) {

		return Optional.ofNullable(articleRepository.findOne(id));
	}

	@Override
	public Optional<Article> findBySlug(String slug, User user) {
		return Optional.ofNullable(articleRepository.findArticleBySlug(slug, user));
	}

	@Override
	public void remove(Article article) {
		articleRepository.delete(article);

	}

}
