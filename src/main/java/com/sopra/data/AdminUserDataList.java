package com.sopra.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
@Getter
public class AdminUserDataList {

	@JsonProperty("usersDetails")
	private List<UserDetails> usersData;

	public AdminUserDataList(List<UserDetails> usersData) {
		super();
		this.usersData = usersData;
	}
	
	
	
}
