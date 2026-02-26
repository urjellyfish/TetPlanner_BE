package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.service.IEmailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerifyCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your verification code");
        message.setText(
                "Your verification code is: " + code +
                        "\n\nThis code expires in 5 minutes."
        );

        mailSender.send(message);
    }

    @Override
    public void sendResetLink(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your verification code");
        message.setText(
                "Your reset password link is: " + code +
                        "\n\nThis link expires in 15 minutes."
        );

        mailSender.send(message);

    }
}
