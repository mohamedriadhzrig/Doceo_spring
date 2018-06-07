package com.sopra.core.article;

import java.util.Optional;

import com.sopra.core.user.User;
import com.sopra.data.AdminArticleDataList;
import com.sopra.data.ArticleDataList;

public interface ArticleService {

	void save(Article article);



	void remove(Article article);

	ArticleDataList findArticles(String author, User user);

	ArticleDataList findValidArticlesByUser(String author, User user);

	ArticleDataList findArticlesByTag(String tag, User user);

	ArticleDataList findFavoriteArticles(String uername, User user);

	ArticleDataList findAllValide(User user);

	AdminArticleDataList findAllInvalide();

	Article findArticleBySlug(String slug);
	
	ArticleDataList getFeed(User user);

}
