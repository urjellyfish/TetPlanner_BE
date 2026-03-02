package com.wecamp.TetPlanner_BE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String name;
    private String accessToken;
    private String refreshToken;
}
