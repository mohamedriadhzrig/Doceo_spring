package com.sopra.core.comment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sopra.core.article.Article;
import com.sopra.core.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	@Id
	@GeneratedValue
    private Long id;
    private String body;
    @ManyToOne
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @ManyToOne
	private Article article;

    
}
