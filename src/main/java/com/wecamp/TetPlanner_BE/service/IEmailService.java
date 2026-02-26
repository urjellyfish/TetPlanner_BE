package com.wecamp.TetPlanner_BE.service;

public interface IEmailService {
    void sendVerifyCode(String to, String code);
}
