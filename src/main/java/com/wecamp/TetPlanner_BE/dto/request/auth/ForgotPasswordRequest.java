package com.wecamp.TetPlanner_BE.dto.request.auth;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
