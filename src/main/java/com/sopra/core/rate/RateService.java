package com.sopra.core.rate;

import java.util.Optional;



public interface RateService {

Optional<Rate> findRateByUsernameInArticle(String username,String slug);
	
	void save(Rate rate);
	
	Double findArticleRatings(String slug);
}
