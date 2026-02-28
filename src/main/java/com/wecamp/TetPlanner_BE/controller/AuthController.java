package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.auth.*;
import com.wecamp.TetPlanner_BE.dto.response.TokenResponse;
import com.wecamp.TetPlanner_BE.service.IAuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        ResponseCookie cookie = setCookieToken(tokenResponse.getRefreshToken());
        return ResponseEntity
                .ok()
                .header("Set-Cookie", cookie.toString())
                .body(new BaseResponse<>(
                        true,
                        "Login successful",
                        tokenResponse
                ));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<TokenResponse>> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        TokenResponse token = authService.refreshToken(refreshToken);
        ResponseCookie cookie = setCookieToken(token.getRefreshToken());
        return ResponseEntity
                .ok()
                .header("Set-cookie", cookie.toString())
                .body(new BaseResponse<>(
                        true,
                        "Token refresh successfully",
                        token
                ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Reset code sent to email",
                null
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<?>> resetPassword(
            @RequestParam String token,
            @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(token, request.getNewPassword());

        return ResponseEntity
                .ok(new BaseResponse<>(
                true,
                "Password reset successfully",
                null
        ));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<BaseResponse<?>> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        try {
            if (refreshToken != null && !refreshToken.isEmpty()) {
                authService.deleteRefreshToken(refreshToken);
            }
            ResponseCookie response = ResponseCookie.from("refreshToken", "")
                    .maxAge(0)
                    .secure(true)
                    .httpOnly(true)
                    .path("/")
                    .sameSite("None")
                    .build();

            return ResponseEntity
                    .ok()
                    .header("Set-cookie", response.toString())
                    .body(new BaseResponse<>(
                            true,
                            "Logout successful",
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new BaseResponse<>(
                            false,
                            "Logout failed: " + e.getMessage(),
                            null
                    ));
        }
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
