package com.sopra.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopra.data.UserData;
import com.sopra.data.UserWithToken;
import com.sopra.entities.Authority;
import com.sopra.entities.Team;
import com.sopra.entities.Theme;
import com.sopra.entities.User;
import com.sopra.exception.InvalidRequestException;
import com.sopra.services.AuthorityService;
import com.sopra.services.TeamService;
import com.sopra.services.UserService;
import com.sopra.utility.EncryptService;
import com.sopra.utility.JwtService;
import com.sopra.utility.MailService;

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
				if (optional.get().getEnabled().equals("false")) {
					bindingResult.rejectValue("Account", "INVALID", "Please confirm your inscription by your Email");
					throw new InvalidRequestException(bindingResult);
				}else {
				Optional<User> user = userService.findById(optional.get().getId());
				Authority authority = authorityService.findAuthorityByName("ADMIN");

				List<String> themes = new ArrayList<String>();
				for (Theme t : user.get().getThemes()) {
					themes.add(t.getName());
				}

				UserData userData = new UserData(null, user.get().getEmail(), user.get().getUsername(),
						user.get().getBio(), user.get().getImage(), user.get().getAuthorities().contains(authority),
						themes, user.get().getTeam().getName() + "");
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
			bindingResult.rejectValue("email", "INVALID", "doesn't exist");
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
		
		Team userTeam = teamService.findTeamByName(registerParam.getTeam());
		
		user.setTeam(userTeam);
		Authority authority = new Authority();
		authority = authorityService.findAuthorityByName("USER");
		user.getAuthorities().add(authority);
		user.setBio(registerParam.getPosition());
		user.setToken(jwtService.toToken(user));
		User u = userService.save(user);
		System.out.println(u.getEnabled());
		// userTeam.getUsers().add(u);
		teamService.save(userTeam);
		UserData userData = new UserData(null, user.getEmail(), user.getUsername(), user.getBio(), user.getImage(),
				false);

		mailService.sendMail(user.getEmail(), "Registration Confirmation",
				"http://localhost:8080/users/regitrationConfirm?token=" + user.getToken());

		return ResponseEntity.status(201).body(
				"please Valide your Email"/* userResponse(new UserWithToken(userData, jwtService.toToken(user))) */);
	}

	@RequestMapping(value = "/users/regitrationConfirm", method = RequestMethod.GET)
	public void confirmRegistration(HttpServletResponse response,@RequestParam("token") String token) {
		Optional<User> user = userService.findUserByToken(token);
		if (user.isPresent()) {
			user.get().setEnabled("true");
			userService.save(user.get());
			try {
				response.sendRedirect("http://localhost:4200/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			try {
				response.sendRedirect("http://localhost:4200/login");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
