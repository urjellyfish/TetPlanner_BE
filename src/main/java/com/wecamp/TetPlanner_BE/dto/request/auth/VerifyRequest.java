package com.wecamp.TetPlanner_BE.dto.request.auth;

import lombok.Data;

@Data
public class VerifyRequest {
    private String email;
    private String code;
}
