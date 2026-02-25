package com.wecamp.TetPlanner_BE.service;

public interface IEmailService {
    void sendVerifyCode(String to, String code);
    void sendResetLink(String to, String code);
}
