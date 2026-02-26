package com.wecamp.TetPlanner_BE.service;

import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;

public interface IAuthService {
    void register(RegisterRequest request);
    void verifyEmail(VerifyRequest request);
    String login(String email, String password);
}
