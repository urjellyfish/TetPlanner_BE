package com.wecamp.TetPlanner_BE.controller;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;
import com.wecamp.TetPlanner_BE.service.IAuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
}
