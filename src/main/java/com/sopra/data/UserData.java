package com.sopra.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
	private String id;
	private String email;
	private String username;
	private String bio;
	private String image;
	private boolean admin;

	
	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
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

	public boolean isAdmin() {
		return admin;
	}

}
