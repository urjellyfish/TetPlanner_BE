package com.wecamp.TetPlanner_BE.exception;

public class InvalidCredential extends RuntimeException {
    public InvalidCredential(String message) {
        super(message);
    }
}
