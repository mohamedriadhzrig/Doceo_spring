package com.sopra.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.core.rate.Rate;
import com.sopra.core.rate.RateService;
import com.sopra.core.user.User;
import com.sopra.repositories.RateRepository;
import com.sopra.repositories.UserRepository;

@Transactional
@Service
public class RateServiceImplementation implements RateService {

	@Autowired
	RateRepository rateRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Optional<Rate> findRateByUsernameInArticle(String username, String slug) {
		User user = userRepository.findUserByUsername(username);
		return Optional.ofNullable(rateRepository.findRateByArticleSlugAndUser(slug, user));
	}

	@Override
	public void save(Rate rate) {
		rateRepository.save(rate);

	}

	@Override
	public Double findArticleRatings(String slug) {
		
		return rateRepository.findArticleRating(slug);
	}

}
