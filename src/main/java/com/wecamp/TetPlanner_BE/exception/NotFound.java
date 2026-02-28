package com.wecamp.TetPlanner_BE.exception;

public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}
