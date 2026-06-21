package com.bablumia.store.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bablumia.store.users.User;
import com.bablumia.store.users.UserNotFoundException;
import com.bablumia.store.users.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                UserNotFoundException::new);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public JwtResponse refreshAccessToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is missing");
        }

        Claims claims = jwtService.parseToken(refreshToken).getPayload();

        Long userId = Long.valueOf(claims.getSubject());

        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String accessToken = jwtService.generateAccessToken(user);

        return new JwtResponse(accessToken);
    }

    public User getCurrentUser() {
        var auth =SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String userIdStr = (String) auth.getPrincipal();
        Long userId = Long.valueOf(userIdStr);

        return userRepository.findById(userId).orElse(null);
    }
}
