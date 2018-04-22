package com.sopra.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData {
    private String id;
    private String body;
    @JsonIgnore
    private String articleId;
    private Date createdAt;
    private Date updatedAt;
    @JsonProperty("author")
    private ProfileData profileData;
}
