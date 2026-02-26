package com.wecamp.TetPlanner_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean isSuccess;
    private String message;
    private T data;
}
