package com.sopra.core.rate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sopra.core.article.Article;
import com.sopra.core.user.User;

@Entity
public class Rate {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private User user;
	private float value=0;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ratedAt=new Date();
	@ManyToOne
	private Article article;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public Date getRatedAt() {
		return ratedAt;
	}
	public void setRatedAt(Date ratedAt) {
		this.ratedAt = ratedAt;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	
	
}
