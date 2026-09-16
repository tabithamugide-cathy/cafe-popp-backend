package com.mugide.cafe_popp_backend.service;

import com.mugide.cafe_popp_backend.dto.LoginRequest;
import com.mugide.cafe_popp_backend.dto.LoginResponse;
import com.mugide.cafe_popp_backend.entity.AppUser;
import com.mugide.cafe_popp_backend.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                       TokenService tokenService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByEmail(request.email())
                .filter(AppUser::isActive)
                .filter(candidate -> passwordEncoder.matches(request.password(), candidate.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        return new LoginResponse(tokenService.issue(user), user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
