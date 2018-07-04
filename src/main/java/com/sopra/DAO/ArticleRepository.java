package com.sopra.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Article;
import com.sopra.entities.Project;
import com.sopra.entities.Tag;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	List<Article> findByUserIsAndStatutIsOrderByCreatedAtDesc(User user, String statut);

	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide')  order by a.createdAt desc ")
	List<Article> findArticlesByTag(@Param("tag") Tag tag);
	
	List<Article> findByProjectIsAndStatutIsOrderByCreatedAtDesc(Project project, String statut);
	
	List<Article> findByThemeIsAndStatutIsOrderByCreatedAtDesc(Theme theme, String statut);
	
	List<Article> findByTeamIsAndStatutIsOrderByCreatedAtDesc(Team team, String statut);
	
	List<Article> findByBodyLikeOrderByCreatedAtDesc(String text, String statut);
	
	
	/********/
	
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.body LIKE :text)  order by a.createdAt desc ")
	List<Article> findArticlesByTagBytext(@Param("text") String text,@Param("tag") Tag tag);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByProject(@Param("project") Project project,@Param("tag") Tag tag);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.theme = :theme)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByTheme(@Param("theme") Theme theme,@Param("tag") Tag tag);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.team = :team)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByTeam(@Param("team") Team team,@Param("tag") Tag tag);
	
	List<Article> findByBodyLikeAndProjectIsAndStatutIsOrderByCreatedAtDesc(String text,Project project, String statut);
	
	List<Article> findByBodyLikeAndThemeIsAndStatutIsOrderByCreatedAtDesc(String text,Theme theme, String statut);
	
	List<Article> findByBodyLikeAndTeamIsAndStatutIsOrderByCreatedAtDesc(String text,Team team, String statut);
	
	List<Article> findByThemeAndTeamAndStatutIsOrderByCreatedAtDesc(Theme theme,Team team, String statut);
	
	List<Article> findByThemeAndProjectAndStatutIsOrderByCreatedAtDesc(Theme theme,Project project, String statut);
	
	List<Article> findByProjectAndTeamAndStatutIsOrderByCreatedAtDesc(Project project,Team team, String statut);
	
	
	/*************/
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project AND a.theme = :theme)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByProjectByTheme(@Param("project") Project project,@Param("tag") Tag tag,@Param("theme") Theme theme);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project AND a.team = :team)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByProjectByTeam(@Param("project") Project project,@Param("tag") Tag tag,@Param("team") Team team);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project AND a.body like :text)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByProjectByText(@Param("project") Project project,@Param("tag") Tag tag,@Param("text") String text);
	
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.theme = :theme AND a.team = :team)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByThemeByTeam(@Param("theme") Theme theme,@Param("tag") Tag tag,@Param("team") Team team);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.theme = :theme AND a.body like :text)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByThemeBytext(@Param("theme") Theme theme,@Param("tag") Tag tag,@Param("text") String text);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.team = :team AND a.body like :text)  order by a.createdAt desc ")
	List<Article> findArticlesByTagByTeamBytext(@Param("team") Team team,@Param("tag") Tag tag,@Param("text") String text);
	
	List<Article> findByBodyLikeAndThemeAndTeamAndStatutIsOrderByCreatedAtDesc(String text,Theme theme,Team team, String statut);
	
	List<Article> findByBodyLikeAndThemeAndProjectAndStatutIsOrderByCreatedAtDesc(String text,Theme theme,Project project, String statut);
	
	List<Article> findByBodyLikeAndProjectAndTeamAndStatutIsOrderByCreatedAtDesc(String text,Project project,Team team, String statut);
	
	List<Article> findByThemeAndTeamAndProjectAndStatutIsOrderByCreatedAtDesc(Theme theme,Team team,Project project, String statut);
	
	/*************/
	
	
	List<Article> findByBodyLikeAndThemeAndTeamAndProjectAndStatutIsOrderByCreatedAtDesc(String text,Theme theme,Team team,Project project, String statut);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project AND a.team = :team AND a.body like :text)  order by a.createdAt desc ")
	List<Article> findArticlesByProjectByTagByTeamBytext(@Param("team") Team team,@Param("tag") Tag tag,@Param("project") Project project,@Param("text") String text);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.team = :team AND a.body like :text AND a.theme = :theme)  order by a.createdAt desc ")
	List<Article> findArticlesBythemeByTagByTeamBytext(@Param("team") Team team,@Param("theme") Theme theme,@Param("tag") Tag tag,@Param("text") String text);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) AND a.statut = 'valide' AND a.project = :project AND a.body like :text AND a.theme = :theme)  order by a.createdAt desc ")
	List<Article> findArticlesBythemeByTagByProjectBytext(@Param("project") Project project,@Param("theme") Theme theme,@Param("tag") Tag tag,@Param("text") String text);
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) "
			+ "AND a.statut = 'valide' AND a.theme = :theme AND a.project = :project AND a.team = :team)  order by a.createdAt desc ")
	List<Article> findArticlesBythemeByTagByProjectByteam(@Param("tag") Tag tag,@Param("theme") Theme theme,@Param("project") Project project,@Param("team") Team team);
	
	
	
	/******/
	
	@Query("select a from Article a,Tag t where (:tag is null  or( a MEMBER OF t.articles AND t = :tag) "
			+ "AND a.statut = 'valide' AND a.theme = :theme AND a.project = :project AND a.team = :team AND a.body LIKE :text)  order by a.createdAt desc ")
	List<Article> findArticlesByAll(@Param("tag") Tag tag,@Param("text") String text,@Param("theme") Theme theme,@Param("project") Project project,@Param("team") Team team);
	
	/******/
	
	Article findArticleBySlug(String slug);

	
	
	
	
	
	
	

	List<Article> findByThemeInAndStatutIsOrderByCreatedAtDesc(List<Theme> theme, String statut);
	
	List<Article> findByThemeAndStatutIsOrderByCreatedAtDesc(Theme theme, String statut);

	List<Article> findByStatutIsOrderByCreatedAtAsc(String statut);

	List<Article> findByStatutIsOrderByCreatedAtDesc(String statut);

	List<Article> findAllByOrderByCreatedAtDesc();

	Long countByCreatedAtBetween(Date date1, Date date2);
}

// (a.tag is null or a.tag.name = :tag) and Select a from article a, tag t where
// a MEMBER OF t.articles;