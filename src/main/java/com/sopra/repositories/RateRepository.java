package com.sopra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sopra.core.article.Article;
import com.sopra.core.rate.Rate;
import com.sopra.core.user.User;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long > {

	@Query("select r from Rate r where (r.article.slug = :x and r.user = :y)")
	Rate findRate(@Param("x") String slug,@Param("y") User  user);
	
	@Query("select AVG(r.value) from Rate r where r.article.slug = :x ")
	Float findArticleRating(@Param("x") String slug);
}
