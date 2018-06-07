package com.sopra.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.core.article.Article;
import com.sopra.core.tag.Tag;
import com.sopra.core.theme.Theme;
import com.sopra.core.user.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	List<Article> findByUserIsAndStatutIsOrderByCreatedAtDesc(User user, String statut);

	Article findArticleBySlug(String slug);

	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide')  order by a.createdAt desc ")
	List<Article> findArticlesByTag(@Param("tag") Tag tag);

	List<Article> findByThemeInAndStatutIsOrderByCreatedAtDesc(List<Theme> theme, String statut);

	List<Article> findByStatutIsOrderByCreatedAtAsc(String statut);

	List<Article> findByStatutIsOrderByCreatedAtDesc(String statut);

	List<Article> findAllByOrderByCreatedAtDesc();

	Long countByCreatedAtBetween(Date date1, Date date2);
}

// (a.tag is null or a.tag.name = :tag) and Select a from article a, tag t where
// a MEMBER OF t.articles;