package com.sopra.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sopra.core.article.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleData {
  
	private Long id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private boolean favorited;
    private int favoritesCount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    private List<Tag> tagList;
    @JsonProperty("author")
    private ProfileData profileData;
}

