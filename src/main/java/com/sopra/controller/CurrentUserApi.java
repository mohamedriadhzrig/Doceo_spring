package com.sopra.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.core.utility.EncryptService;
import com.sopra.data.UserData;
import com.sopra.data.UserWithToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController
@RequestMapping(path = "/user")
public class CurrentUserApi {
	@Autowired
	private UserService userService;

	@Autowired
	private EncryptService encryptService;

	@GetMapping
	public ResponseEntity currentUser(@AuthenticationPrincipal User currentUser,
			@RequestHeader(value = "Authorization") String authorization) {
		Optional<User> user = userService.findById(currentUser.getId());
		UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(), user.get().getBio(),
				user.get().getImage());
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, authorization.split(" ")[1])));
	}

	@PutMapping
	public ResponseEntity updateProfile(@Valid @RequestBody UpdateUserParam updateUserParam,
			@AuthenticationPrincipal User currentUser, @RequestHeader("Authorization") String token,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		
		if (!updateUserParam.getPassword().equals(""))
			currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(),
					encryptService.encrypt(updateUserParam.getPassword()), updateUserParam.getBio(),
					currentUser.getImage());
		else {
			if (updateUserParam.getImage().equals(""))
				currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(), currentUser.getPassword(),
						updateUserParam.getBio(), currentUser.getImage());
			else
				currentUser.update(updateUserParam.getEmail(), updateUserParam.getUsername(), currentUser.getPassword(),
						updateUserParam.getBio(), updateUserParam.getImage());
		}
		userService.save(currentUser);
		Optional<User> user = userService.findById(currentUser.getId());
		UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(), user.get().getBio(),
				user.get().getImage());
		return ResponseEntity.ok(userResponse(new UserWithToken(userData, token.split(" ")[1])));
	}

	private void checkUniquenessOfUsernameAndEmail(User currentUser, UpdateUserParam updateUserParam,
			BindingResult bindingResult) {
		if (!"".equals(updateUserParam.getUsername())) {
			Optional<User> byUsername = userService.findByUsername(updateUserParam.getUsername());
			if (byUsername.isPresent() && !byUsername.get().equals(currentUser)) {
				bindingResult.rejectValue("username", "DUPLICATED", "username already exist");
			}
		}

		if (!"".equals(updateUserParam.getEmail())) {
			Optional<User> byEmail = userService.findByEmail(updateUserParam.getEmail());
			if (byEmail.isPresent() && !byEmail.get().equals(currentUser)) {
				bindingResult.rejectValue("email", "DUPLICATED", "email already exist");
			}
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	private Map<String, Object> userResponse(UserWithToken userWithToken) {
		return new HashMap<String, Object>() {
			{
				put("user", userWithToken);
			}
		};
	}
}

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserParam {
	private String bio = "";
	private String email = "";
	private String image = "";
	private String password = "";
	private String username = "";

}
