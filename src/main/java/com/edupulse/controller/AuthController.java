package com.edupulse.controller;

import com.edupulse.dto.ApiResponse;
import com.edupulse.dto.ChangePasswordRequest;
import com.edupulse.dto.LoginRequest;
import com.edupulse.dto.LoginResponse;
import com.edupulse.dto.UserSummaryDto;
import com.edupulse.entity.User;
import com.edupulse.exception.BadRequestException;
import com.edupulse.security.JwtService;
import com.edupulse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getCurrentUser(authentication);
        String token = jwtService.generateToken(user.getUsername());
        UserSummaryDto summary = userService.toSummary(user);
        return ResponseEntity.ok(new ApiResponse(true, "Login successful",
                new LoginResponse(token, "Bearer", summary)));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<ApiResponse> me(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("Not authenticated");
        }
        User user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(new ApiResponse(true, "User profile loaded", userService.toSummary(user)));
    }

    @PostMapping("/auth/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        userService.changePassword(user, request.currentPassword(), request.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully", null));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully", null));
    }
}
