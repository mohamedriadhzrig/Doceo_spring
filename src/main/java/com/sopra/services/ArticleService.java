package com.sopra.services;

import com.sopra.data.AdminArticleDataList;
import com.sopra.data.ArticleDataList;
import com.sopra.entities.Article;
import com.sopra.entities.Project;
import com.sopra.entities.Tag;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;

public interface ArticleService {

	void save(Article article);

	void remove(Article article);
	
	ArticleDataList findArticlesBy(String text,Project project,Team team,Theme theme,Tag tag,User user);

	ArticleDataList findArticles(String author, User user);

	ArticleDataList findValidArticlesByUser(String author, User user);

	ArticleDataList findArticlesByTag(String tag, User user);
	
	ArticleDataList findArticlesByTheme(String theme, User user);
	
	ArticleDataList findArticlesByTagAndTheme(String tag,String theme, User user);

	ArticleDataList findFavoriteArticles(String uername, User user);

	ArticleDataList findAllValide(User user);

	AdminArticleDataList findAllInvalide();

	Article findArticleBySlug(String slug);

	ArticleDataList getFeed(User user);

}
