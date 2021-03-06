package com.sopra.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Document(indexName = "article", type = "article", shards = 1)
public class Article implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String slug;
	private String title;
	private String category;
	private String description;
	@Type(type = "text")
	private String body;
	private String fileType;
	@JsonIgnore
	@ManyToOne
	private Team team;
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })

	@JsonIgnore
	private List<Tag> tags = new ArrayList<Tag>();
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, mappedBy = "favoriteArticles")
	private List<User> likedBy = new ArrayList<User>();
	@Temporal(TemporalType.DATE)
	private Date createdAt = new Date();
	@Temporal(TemporalType.DATE)
	private Date validatedAt;
	private String path;
	private int seen = 0;
	private String statut;
	@JsonIgnore
	@ManyToOne
	private User user;
	@JsonIgnore
	@ManyToOne
	private Project project;
	@JsonIgnore
	@ManyToOne
	private Theme theme;
	@JsonIgnore
	@OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Comment> comments;
	@JsonIgnore
	@OneToMany(mappedBy = "article")
	private List<Rate> ratings = new ArrayList<Rate>();

	@JsonIgnore
	public List<Rate> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rate> ratings) {
		this.ratings = ratings;
	}

	public List<User> getLikedBy() {
		return likedBy;
	}

	public void setLikedBy(List<User> likedBy) {
		this.likedBy = likedBy;
	}

	public Date getValidatedAt() {
		return validatedAt;
	}

	public void setValidatedAt(Date validatedAt) {
		this.validatedAt = validatedAt;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

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
		return validatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.validatedAt = updatedAt;
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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
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
		this.validatedAt = updatedAt;
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

	public Article(String title, String description, String body, User userId) {
		super();
		this.slug = toSlug(title);
		this.title = title;
		this.description = description;
		this.body = body;

		this.user = userId;
	}

	private String toSlug(String title) {
		List rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1));
		PasswordGenerator generator = new PasswordGenerator();
		String id = generator.generatePassword(8, rules);

		return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\â€™|\\â€?|\\s\\?\\,\\.]+", "-") + id;
	}
}
