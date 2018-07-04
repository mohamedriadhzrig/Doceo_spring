package com.sopra.services;

import java.util.Optional;

import com.sopra.entities.Rate;



public interface RateService {

Optional<Rate> findRateByUsernameInArticle(String username,String slug);
	
	void save(Rate rate);
	
	Double findArticleRatings(String slug);
}
