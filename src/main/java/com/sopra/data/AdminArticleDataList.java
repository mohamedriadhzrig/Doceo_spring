package com.sopra.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AdminArticleDataList {

	@JsonProperty("articles")
	private List<ArticleData> articleData;
	@JsonProperty("articlesCount")
	private int countInvalid;
	@JsonProperty("allArticlesCount")
	private Long countAll;
	@JsonProperty("countWeek")
	private Long countWeek;

	public AdminArticleDataList(List<ArticleData> articleData, int countInvalid, Long countAll) {
		super();
		this.articleData = articleData;
		this.countInvalid = countInvalid;
		this.countAll = countAll;
	}

	public AdminArticleDataList(List<ArticleData> articleData, int countInvalid, Long countAll, Long countWeek) {
		super();
		this.articleData = articleData;
		this.countInvalid = countInvalid;
		this.countAll = countAll;
		this.countWeek = countWeek;
	}
}
