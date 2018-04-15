package com.sopra.data;




public class UserWithToken {
    private String email;
    private String username="try";
    private String bio;
    private String image;
    private String token;

    public UserWithToken(UserData userData, String token) {
        this.email = userData.getEmail();
        this.username = userData.getUsername();
        this.bio = userData.getBio();
        this.image = userData.getImage();
        this.token = token;
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

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
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
    

}
