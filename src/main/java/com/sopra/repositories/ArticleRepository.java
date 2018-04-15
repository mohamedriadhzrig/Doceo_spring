package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.core.article.Article;
import com.sopra.core.user.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
		@Query("select a from Article a where a.slug = :x and a.user = :y")
		Article findArticleBySlug(@Param("x")String slug,@Param("y")User user);
		
		
}
