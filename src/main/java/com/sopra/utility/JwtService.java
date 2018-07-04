package com.sopra.utility;

import org.springframework.stereotype.Service;

import com.sopra.entities.User;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
