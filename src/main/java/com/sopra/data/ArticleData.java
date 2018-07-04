package com.sopra.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sopra.entities.Tag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {

	private Long id;
	private String slug;
	private String title;
	private String description;
	private String body;
	private String fileType;
	private int seen;
	private boolean favorited;
	private int favoritesCount;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	private List<Tag> tagList;
	@JsonProperty("author")
	private ProfileData profileData;
	private Double rating=0.0;

	
	public Long getId() {
		return id;
	}

	public String getSlug() {
		return slug;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getBody() {
		return body;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public int getFavoritesCount() {
		return favoritesCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public List<String> getTagList() {
		List<String> list = new ArrayList<String>();
		for (Tag t : tagList) {
			list.add(t.getName());
		}
		return list;
	}

	public ProfileData getProfileData() {
		return profileData;
	}

	public int getSeen() {
		return seen;
	}

	public String getFileType() {
		return fileType;
	}

	public Double getRating() {
		return rating;
	}

	

	
}
