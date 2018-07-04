package com.sopra.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.DAO.RateRepository;
import com.sopra.DAO.UserRepository;
import com.sopra.entities.Rate;
import com.sopra.entities.User;
import com.sopra.services.RateService;

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
