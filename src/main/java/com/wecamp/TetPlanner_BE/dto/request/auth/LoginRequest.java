package com.wecamp.TetPlanner_BE.dto.request.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
