package com.sopra.utility;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sopra.entities.User;
import com.sopra.services.UserService;

@Service
public class MailService {
	@Value("${spring.mail.username}")
	private String email;

	private JavaMailSender javaMailSender;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService accountService;

	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void resetPasswordMail(User user) throws MailException {
		List rules = Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
				new CharacterRule(EnglishCharacterData.LowerCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1));
		PasswordGenerator generator = new PasswordGenerator();
		String password = generator.generatePassword(8, rules);
		String hashPW = bCryptPasswordEncoder.encode(password);
		accountService.updatePaswword(hashPW, user.getId());
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(this.email);
		mail.setSubject("Password Recovery");
		mail.setText("new password: " + password);
		javaMailSender.send(mail);
	}

	public void sendMail(String email, String subject, String body) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email);
		mail.setFrom(this.email);
		mail.setSubject(subject);
		mail.setText(body);
		javaMailSender.send(mail);
	}

}
