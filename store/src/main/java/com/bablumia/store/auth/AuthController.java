package com.bablumia.store.auth;

import jakarta.servlet.http.Cookie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bablumia.store.users.UserDto;
import com.bablumia.store.users.UserMapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        var loginResponse = authService.login(request);

        var refreshToken = loginResponse.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResponse.getAccessToken().toString());
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@CookieValue("refreshToken") String refreshToken) {
        System.out.println("Received refresh token: " + refreshToken);
        var newAccessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(newAccessToken.toString());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
}
