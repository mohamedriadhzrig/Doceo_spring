package com.sopra.utility;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.DAO.ArticleRepository;
import com.sopra.ElasticRepositories.SearchRepository;
import com.sopra.entities.Article;

@Component
public class Loaders {

	@Autowired
	ElasticsearchOperations operations;

	@Autowired
	SearchRepository searchRepository;

	@Autowired
	ArticleRepository articleRepository;

	@PostConstruct
	@Transactional
	public void loadAll() {

		operations.putMapping(Article.class);
		System.out.println("Loading Data");
		List<Article> data = articleRepository.findAll();
		searchRepository.save(data); // loads into Elastic
		System.out.printf("Loading Completed");

	}

}
