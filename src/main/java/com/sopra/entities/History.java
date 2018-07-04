package com.sopra.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class History {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private String articleSlug;
	@Temporal(TemporalType.TIMESTAMP)
	private Date seenAt = new Date();
	public History(User user, String articleSlug) {
		super();
		this.user = user;
		this.articleSlug = articleSlug;
		this.seenAt=new Date();
	}
	

	
	

}
