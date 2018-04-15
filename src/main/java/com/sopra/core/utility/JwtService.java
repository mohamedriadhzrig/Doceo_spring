package com.sopra.core.utility;

import org.springframework.stereotype.Service;

import com.sopra.core.user.User;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
