package com.sopra.data;

import java.util.ArrayList;
import java.util.List;

public class UserWithToken {
	private String email;
	private String username = "try";
	private String position;
	private String image;
	private String token;
	private String team;
	private boolean admin;
	private List<String> themeList = new ArrayList<String>();

	public UserWithToken(UserData userData, String token) {
		this.email = userData.getEmail();
		this.username = userData.getUsername();
		this.position = userData.getPosition();
		this.image = userData.getImage();
		this.token = token;
		this.team = userData.getTeam();
		this.admin = userData.isAdmin();
		this.themeList = userData.getThemeList();
	}
	
	

	public List<String> getThemeList() {
		return themeList;
	}



	public void setThemeList(List<String> themeList) {
		this.themeList = themeList;
	}



	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = "try";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

}
