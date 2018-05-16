package com.sopra.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.core.article.Article;
import com.sopra.core.tag.Tag;
import com.sopra.core.user.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	@Query("select a from Article a where a.slug = :x and a.user = :y")
	Article findBySlug(@Param("x") String slug, @Param("y") User user);

	@Query("select a from Article a where (a.user.username is null or a.user.username='' or a.user.username = :author) order by a.createdAt desc")
	List<Article> findArticles(@Param("author") String author);
	
	@Query("select a from Article a where (a.user.username = :author and a.statut = 'valide') order by a.createdAt desc")
	List<Article> findValidArticlesByUser(@Param("author") String author);

	Article findArticleBySlug(String slug);

	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide')  order by a.createdAt desc ")
	List<Article> findArticlesByTag(@Param("tag") Tag tag);
	
	@Query("select a from Article a where a.statut = 'valide' order by a.createdAt desc")
	List<Article> findValideArticle();
	
	@Query("select a from Article a where (a.statut != 'valide' )order by a.createdAt asc")
	List<Article> findAllInvalide();
	
	List<Article> findAllByOrderByCreatedAtDesc();
}

// (a.tag is null or a.tag.name = :tag) and Select a from article a, tag t where
// a MEMBER OF t.articles;