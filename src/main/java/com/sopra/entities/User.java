package com.sopra.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "USER")
public class User implements UserDetails, Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@JsonIgnore
	@Column(name = "password")
	private String password;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "image")
	private String image;

	@Column(name = "bio")
	private String bio = "";

	@OneToMany(mappedBy = "user")
	private List<Rate> rate;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="user")
	private List<History> histories=new ArrayList<History>();
	
	@ManyToOne
	private Team team;

	public List<Rate> getRate() {
		return rate;
	}

	public void setRate(List<Rate> rate) {
		this.rate = rate;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities = new ArrayList<Authority>();

	@OneToMany(mappedBy = "user")
	private List<Comment> comments;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Article> favoriteArticles;

	@OneToMany(mappedBy = "user")
	private List<Article> articles;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Theme> themes;

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public List<Article> getFavoriteArticles() {
		return favoriteArticles;
	}

	public void setFavoriteArticles(List<Article> favoriteArticles) {
		this.favoriteArticles = favoriteArticles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	
	public List<History> getHistories() {
		return histories;
	}

	public void setHistories(List<History> histories) {
		this.histories = histories;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Article> getArticle() {
		return articles;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setArticle(List<Article> article) {
		this.articles = article;
	}

	public void update(String email, String username, String password, String bio, String image) {
		if (!"".equals(email)) {
			this.email = email;
		}

		if (!"".equals(username)) {
			this.username = username;
		}

		if (!"".equals(password)) {
			this.password = password;
		}

		if (!"".equals(bio)) {
			this.bio = bio;
		}

		if (!"".equals(image)) {
			this.image = image;
		}
	}

	public User(String email, String username, String password, String bio, String image) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
		this.bio = bio;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {

		this.lastname = lastname;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	public List<Authority> getAuthorities() {
		return this.authorities;
	}

	// We can add the below fields in the users table.
	// For now, they are hardcoded.
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", authorities=" + authorities + "]";
	}

	public User(Long id, String username, String email, String password, String firstname, String lastname,
			List<Authority> authorities, List<Article> articles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.authorities = authorities;
		this.articles = articles;
	}

	public User() {
		super();
	}

}
