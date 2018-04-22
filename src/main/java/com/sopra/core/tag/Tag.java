package com.sopra.core.tag;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopra.core.article.Article;

@Entity
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "tags")
	private List<Article> articles = new ArrayList<Article>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tag(Long id, String name) {
		super();
		this.id = id;
		this.name = name;

	}

	public Tag(String name) {
		super();

		this.name = name;

	}

	public Tag() {
		super();
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> article) {
		this.articles = article;
	}

}
