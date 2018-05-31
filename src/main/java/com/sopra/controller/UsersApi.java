package com.sopra.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.api.exception.InvalidRequestException;
import com.sopra.core.authority.Authority;
import com.sopra.core.authority.AuthorityService;
import com.sopra.core.team.Team;
import com.sopra.core.team.TeamService;
import com.sopra.core.user.User;
import com.sopra.core.user.UserService;
import com.sopra.core.utility.EncryptService;
import com.sopra.core.utility.JwtService;
import com.sopra.core.utility.MailService;
import com.sopra.data.UserData;
import com.sopra.data.UserWithToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController
public class UsersApi {
	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private EncryptService encryptService;
	@Autowired
	private JwtService jwtService;

	private void checkInput(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
		if (userService.findByUsername(registerParam.getUsername()).isPresent()) {
			bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
			throw new InvalidRequestException(bindingResult);
		}

		if (userService.findByEmail(registerParam.getEmail()).isPresent()) {
			bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
			throw new InvalidRequestException(bindingResult);
		}

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException(bindingResult);
		}
	}

	@RequestMapping(path = "/users/login", method = POST)
	public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam, BindingResult bindingResult) {
		Optional<User> optional = userService.findByEmail(loginParam.getEmail());
		if (optional.isPresent() && encryptService.check(loginParam.getPassword(), optional.get().getPassword())) {
			Optional<User> user = userService.findById(optional.get().getId());
			Authority authority = authorityService.findAuthorityByName("ADMIN");
			if (user.get().getAuthorities().contains(authority)) {
				UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(),
						user.get().getBio(), user.get().getImage(), true);
				return ResponseEntity.ok(userResponse(new UserWithToken(userData, jwtService.toToken(optional.get()))));
			} else {
				UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(),
						user.get().getBio(), user.get().getImage(), false);
				return ResponseEntity.ok(userResponse(new UserWithToken(userData, jwtService.toToken(optional.get()))));
			}

		} else {
			bindingResult.rejectValue("password", "INVALID", "invalid email or password");
			throw new InvalidRequestException(bindingResult);
		}
	}

	

	@PostMapping("/forgetpassword")
	public ResponseEntity recoverPassword(@Valid @RequestBody PasswordRecovery passwordRecovery,
			BindingResult bindingResult) {

		Optional<User> user = userService.findByEmail(passwordRecovery.getEmail());
		if (!user.isPresent()) {
			bindingResult.rejectValue("email", "INVALID", "this email doesn't exist");
			throw new InvalidRequestException(bindingResult);
		} else {

			mailService.resetPasswordMail(user.get());
			return ResponseEntity.ok("mail sent");
		}

	}

	private Map<String, Object> userResponse(UserWithToken userWithToken) {
		return new HashMap<String, Object>() {
			{
				put("user", userWithToken);
			}
		};
	}

	@RequestMapping(path = "/users", method = POST)
	public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("Error ::: " + bindingResult.toString());
			System.out.println(registerParam.toString());
		}

		checkInput(registerParam, bindingResult);

		User user = new User(registerParam.getEmail(), registerParam.getUsername(),
				encryptService.encrypt(registerParam.getPassword()), "", "default.png");
		System.out.println(registerParam.getTeam());
		Team userTeam = teamService.findTeamByName(registerParam.getTeam());
		System.out.println(userTeam.getName());
		user.setTeam(userTeam);
		Authority authority = new Authority();
		authority = authorityService.findAuthorityByName("USER");
		user.getAuthorities().add(authority);
		user.setBio(registerParam.getPosition());

		User u = userService.save(user);
		// userTeam.getUsers().add(u);
		teamService.save(userTeam);
		UserData userData = new UserData(null, user.getEmail(), user.getUsername(), user.getBio(), user.getImage(),
				false);
		return ResponseEntity.status(201).body(userResponse(new UserWithToken(userData, jwtService.toToken(user))));
	}
}

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class RegisterParam {
	@NotBlank(message = "can't be empty")
	private String password;
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	private String email;
	@NotBlank(message = "can't be empty")
	private String username;
	@NotBlank(message = "can't be empty")
	private String team;
	@NotBlank(message = "can't be empty")
	private String position;

}

@Getter
@NoArgsConstructor
@ToString
class LoginParam {
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	private String email;
	@NotBlank(message = "can't be empty")
	private String password;
}

@Getter
@NoArgsConstructor
@ToString
class PasswordRecovery {
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	private String email;

}
