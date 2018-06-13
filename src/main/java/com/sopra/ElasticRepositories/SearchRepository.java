package com.sopra.ElasticRepositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.sopra.core.article.Article;

public interface SearchRepository extends ElasticsearchRepository<Article, Long> {

	

}
