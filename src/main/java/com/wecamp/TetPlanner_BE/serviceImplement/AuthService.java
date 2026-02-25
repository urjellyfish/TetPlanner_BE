package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.service.IAuthService;
import com.wecamp.TetPlanner_BE.service.IEmailService;
import com.wecamp.TetPlanner_BE.service.IRedisService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Data
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IRedisService redisService;
    private final IEmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        String code = generateCode();
        redisService.save("verify_code:" + request.getEmail(), code, 300);

        redisService.save("verify_password:" + request.getEmail(),
                passwordEncoder.encode(request.getPassword()), 300);

        redisService.save("verify_fullname:" + request.getEmail(),
                request.getFullName(), 300);

        System.out.println("Verification code: " + code);
        emailService.sendVerifyCode(request.getEmail(), code);
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyRequest request) {
        String savedCode =
                redisService.get("verify_code:" + request.getEmail());

        if (savedCode == null) {
            throw new RuntimeException("Code expired");
        }

        if (!savedCode.equals(request.getCode())) {
            throw new RuntimeException("Invalid code");
        }

        String password =
                redisService.get("verify_password:" + request.getEmail());

        String fullName =
                redisService.get("verify_fullname:" + request.getEmail());

        User user = new User();

        user.setEmail(request.getEmail());
        user.setHashPassword(password);
        user.setFullName(fullName);
        user.setEmailVerified(true);

        userRepository.save(user);

        redisService.delete("verify_code:" + request.getEmail());
        redisService.delete("verify_password:" + request.getEmail());
        redisService.delete("verify_fullname:" + request.getEmail());
    }

    @Override
    public String login(String email, String password) {
        return "";
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(9000) + 1000);
    }
}
