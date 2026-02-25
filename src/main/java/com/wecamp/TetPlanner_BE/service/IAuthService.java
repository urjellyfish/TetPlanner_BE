package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.LoginRequest;
import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;
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
