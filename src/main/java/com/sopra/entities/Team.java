package com.sopra.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@OneToMany(mappedBy = "team")
	private List<User> users=new ArrayList<User>();
	@OneToMany(mappedBy = "team")
	private List<Article> articles=new ArrayList<Article>();
	public Team(String name) {
		super();
		this.name = name;
	}

}
