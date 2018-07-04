package com.sopra.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.entities.Article;
import com.sopra.entities.Rate;
import com.sopra.entities.User;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {


	
	@Query("select AVG(r.value) from Rate r where r.article.slug = :x ")
	Double findArticleRating(@Param("x") String slug);
	
	Rate findRateByArticleSlugAndUser(String slug,User user);
}
