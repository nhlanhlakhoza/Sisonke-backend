package com.example.Sisonke.backend.controller;

import com.example.Sisonke.backend.Repository.UserRepository;
import com.example.Sisonke.backend.dto.request.LoginRequest;
import com.example.Sisonke.backend.dto.request.RegisterRequest;
import com.example.Sisonke.backend.dto.response.UserResponse;
import com.example.Sisonke.backend.models.user.entity.User;
import com.example.Sisonke.backend.service.JwtService;
import com.example.Sisonke.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
        @RequestBody RegisterRequest request
    ) {

        User user = userService.register(
            request.getFullName(),
            request.getEmail(),
            request.getPassword()
        );

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Prepare response
        UserResponse response = new UserResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            token
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
        @RequestBody LoginRequest request
    ) {

        // 1. Find user by email
        User user = userRepository
            .findByEmailIgnoreCase(request.getEmail())
            .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password."
            ));

        // 2. Verify password
        if (!passwordEncoder.matches(
            request.getPassword(),
            user.getPasswordHash()
        )) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid email or password."
            );
        }

        // 3. Generate JWT token
        String token = jwtService.generateToken(user);

        // 4. Prepare response
        UserResponse response = new UserResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            token
        );

        return ResponseEntity.ok(response);
    }
}
