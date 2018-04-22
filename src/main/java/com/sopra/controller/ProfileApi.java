package com.sopra.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.data.ProfileData;

@RestController
public class ProfileApi {
	@Autowired
	private UserService userService;

	@GetMapping("profiles/{username}")
	public ResponseEntity getProfile(@PathVariable("username") String username, @AuthenticationPrincipal User user) {

		Optional<User> profile = userService.findByUsername(username);
		ProfileData profileData = new ProfileData(profile.get().getId(), profile.get().getUsername(),
				profile.get().getBio(), profile.get().getImage(), false);
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
