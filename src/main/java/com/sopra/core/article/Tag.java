package com.sopra.core.article;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Tag {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@ManyToOne
	private Article article;
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
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public Tag(Long id, String name, Article article) {
		super();
		this.id = id;
		this.name = name;
		this.article = article;
	}
	public Tag() {
		super();
	}

	
}
