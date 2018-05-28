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
	private String position;
	private String image;
	private String team;
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

	public String getPosition() {
		return position;
	}

	public String getImage() {
		return "http://localhost:8080/files/" + image;
	}

	public boolean isAdmin() {
		return admin;
	}

	public String getTeam() {
		return team;
	}

	public UserData(String id, String email, String username, String bio, String image, boolean admin) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.position = bio;
		this.image = image;
		this.admin = admin;
	}

}
