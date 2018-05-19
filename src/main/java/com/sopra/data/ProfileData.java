package com.sopra.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
	@JsonIgnore
	private Long id;
	private String username;
	private String bio;
	private String image;
	private boolean admin;

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getBio() {
		return bio;
	}

	public String getImage() {
		return "http://localhost:8080/files/" + image;
	}

	public boolean getAdmin() {
		return admin;
	}

}
