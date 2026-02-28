package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.auth.LoginRequest;
import com.wecamp.TetPlanner_BE.dto.request.auth.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.auth.VerifyRequest;
import com.wecamp.TetPlanner_BE.dto.response.TokenResponse;

public interface IAuthService {
    void register(RegisterRequest request);
    void verifyEmail(VerifyRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse refreshToken(String refreshToken);
    void deleteRefreshToken(String token);

    void sendResetLink(String email);
    void resetPassword(String token, String newPassword);
}
