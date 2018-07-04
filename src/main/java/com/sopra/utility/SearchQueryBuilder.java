package com.sopra.utility;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import com.sopra.entities.Article;

@Component
public class SearchQueryBuilder {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	public List<Article> getAll(String text) {

		QueryBuilder query = QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery(text).lenient(true).field("title").field("category")
						.field("description").field("body"))
				.must(QueryBuilders.queryStringQuery("valide").lenient(true).field("statut")

				).must(QueryBuilders.queryStringQuery("*" + text + "*").lenient(true).field("title").field("category")
						.field("description").field("body"));

		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();

		List<Article> articles = elasticsearchTemplate.queryForList(build, Article.class);

		return articles;
	}

	public List<Article> getTitles(String text) {

		QueryBuilder query = QueryBuilders.boolQuery()
				.should(QueryBuilders.queryStringQuery(text).lenient(true).field("title")

				).must(QueryBuilders.queryStringQuery("valide").lenient(true).field("statut")

				).must(QueryBuilders.queryStringQuery("*" + text + "*").lenient(true).field("title")

		);

		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();

		List<Article> articles = elasticsearchTemplate.queryForList(build, Article.class);

		return articles;
	}
}
