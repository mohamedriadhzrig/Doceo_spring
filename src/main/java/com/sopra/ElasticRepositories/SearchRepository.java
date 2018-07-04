package com.sopra.ElasticRepositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.sopra.entities.Article;

public interface SearchRepository extends ElasticsearchRepository<Article, Long> {

	

}
