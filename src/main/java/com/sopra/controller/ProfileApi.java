package com.sopra.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.data.ProfileData;
import com.sopra.entities.Authority;
import com.sopra.entities.User;
import com.sopra.services.AuthorityService;
import com.sopra.services.UserService;

@RestController
public class ProfileApi {
	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authorityService;

	@GetMapping("profiles/{username}")
	public ResponseEntity getProfile(@PathVariable("username") String username, @AuthenticationPrincipal User user) {

		Authority authority = authorityService.findAuthorityByName("ADMIN");
		Optional<User> profile = userService.findByUsername(username);
		ProfileData profileData = new ProfileData(profile.get().getId(), profile.get().getUsername(),
				profile.get().getBio(), profile.get().getImage(), profile.get().getAuthorities().contains(authority));
		return ResponseEntity.ok(profileResponse(profileData));

	}

	@PostMapping(value = "profiles/{username}/upgrade")
	public ResponseEntity upgradeUserToAdmin(@PathVariable("username") String username,
			@AuthenticationPrincipal User user) {
		Authority authority = authorityService.findAuthorityByName("ADMIN");
		User u = new User();
		u = userService.findByUsername(username).get();
		if (!u.getAuthorities().contains(authority)) {
			u.getAuthorities().add(authority);
			userService.save(u);
		}
		ProfileData profileData = new ProfileData();
		profileData.setBio(u.getBio());
		profileData.setUsername(u.getUsername());
		profileData.setImage(u.getImage());
		profileData.setId(u.getId());
		profileData.setAdmin(true);
		return ResponseEntity.ok(profileResponse(profileData));
	}

	@PostMapping(value = "profiles/{username}/downgrade")
	public ResponseEntity downgradeUserToAdmin(@PathVariable("username") String username,
			@AuthenticationPrincipal User user) {
		Authority authority = authorityService.findAuthorityByName("ADMIN");
		User u = new User();
		u = userService.findByUsername(username).get();
		if (u.getAuthorities().contains(authority)) {
			u.getAuthorities().remove(authority);
			userService.save(u);
		}
		ProfileData profileData = new ProfileData();
		profileData.setBio(u.getBio());
		profileData.setUsername(u.getUsername());
		profileData.setImage(u.getImage());
		profileData.setId(u.getId());
		profileData.setAdmin(false);
		return ResponseEntity.ok(profileResponse(profileData));
	}

	/*
	 * @PostMapping(path = "follow") public ResponseEntity
	 * follow(@PathVariable("username") String username,
	 * 
	 * @AuthenticationPrincipal User user) { return
	 * userRepository.findByUsername(username).map(target -> { FollowRelation
	 * followRelation = new FollowRelation(user.getId(), target.getId());
	 * userRepository.saveRelation(followRelation); return
	 * profileResponse(profileQueryService.findByUsername(username, user).get());
	 * }).orElseThrow(ResourceNotFoundException::new); }
	 * 
	 * @DeleteMapping(path = "follow") public ResponseEntity
	 * unfollow(@PathVariable("username") String username,
	 * 
	 * @AuthenticationPrincipal User user) { Optional<User> userOptional =
	 * userRepository.findByUsername(username); if (userOptional.isPresent()) { User
	 * target = userOptional.get(); return userRepository.findRelation(user.getId(),
	 * target.getId()) .map(relation -> { userRepository.removeRelation(relation);
	 * return profileResponse(profileQueryService.findByUsername(username,
	 * user).get()); }).orElseThrow(ResourceNotFoundException::new); } else { throw
	 * new ResourceNotFoundException(); } }
	 */
	private Map<String, Object> profileResponse(ProfileData profile) {
		return new HashMap<String, Object>() {
			{
				put("profile", profile);
			}
		};
	}
}
