package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.LoginRequest;
import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;
import com.wecamp.TetPlanner_BE.dto.response.TokenResponse;
import com.wecamp.TetPlanner_BE.service.IAuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<?>> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                true,
                "Verification code sent to email",
                null
        ));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<BaseResponse<?>> verifyEmail(@RequestBody VerifyRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Register successful",
                        null
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<?>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        ResponseCookie cookie = setCookieToken(tokenResponse.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Set-Cookie", cookie.toString())
                .body(new BaseResponse<>(
                        true,
                        "Login successful",
                        tokenResponse
                ));
    }

    private ResponseCookie setCookieToken(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(7 * 24 * 60 * 60)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();
    }
}
