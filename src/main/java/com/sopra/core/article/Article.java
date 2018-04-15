package com.sopra.core.article;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sopra.core.user.User;

@Entity
public class Article implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String slug;
	private String title;
	private String category;
	private String description;
	private String body;
	@OneToMany(mappedBy="article")
	private List<Tag> tags;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	private String path;
	private int seen;
	private String statut;
	@ManyToOne
	private User user;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSeen() {
		return seen;
	}

	public void setSeen(int seen) {
		this.seen = seen;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Article(Long id, String slug, String title, String category, String description, String body, List<Tag> tags,
			Date createdAt, Date updatedAt, String path, int seen, String statut, User userId) {
		super();
		this.id = id;
		this.slug = toSlug(title);
		this.title = title;
		this.category = category;
		this.description = description;
		this.body = body;
		this.tags = tags;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.path = path;
		this.seen = seen;
		this.statut = statut;
		this.user = userId;
	}

	
	public Article() {
		super();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Article(String title, String description, String body, List<Tag> tags, User userId) {
		super();
		this.slug = toSlug(title);
		this.title = title;
		this.description = description;
		this.body = body;
		this.tags = tags;
		this.user = userId;
	}

	private String toSlug(String title) {
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\â€™|\\â€?|\\s\\?\\,\\.]+", "-");
    }
}
