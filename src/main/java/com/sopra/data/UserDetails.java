package com.sopra.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
	private String email;
	private String username;
	private boolean admin;
	private Long articleCount;
	private Long commentCount;
	private Long demandCount;
	private String team;
}
