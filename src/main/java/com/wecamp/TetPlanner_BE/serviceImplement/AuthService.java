package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.dto.request.LoginRequest;
import com.wecamp.TetPlanner_BE.dto.request.RegisterRequest;
import com.wecamp.TetPlanner_BE.dto.request.VerifyRequest;
import com.wecamp.TetPlanner_BE.dto.response.TokenResponse;
import com.wecamp.TetPlanner_BE.entity.RefreshToken;
import com.wecamp.TetPlanner_BE.entity.User;
import com.wecamp.TetPlanner_BE.exception.InvalidCredential;
import com.wecamp.TetPlanner_BE.repository.RefreshTokenRepository;
import com.wecamp.TetPlanner_BE.repository.UserRepository;
import com.wecamp.TetPlanner_BE.security.JwtUtil;
import com.wecamp.TetPlanner_BE.service.IAuthService;
import com.wecamp.TetPlanner_BE.service.IEmailService;
import com.wecamp.TetPlanner_BE.service.IRedisService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Data
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final IRedisService redisService;
    private final IEmailService emailService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredential("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            throw new InvalidCredential("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        RefreshToken refreshToken = jwtUtil.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        RefreshToken oldToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new InvalidCredential("Invalid refresh token"));
        if (LocalDateTime.now().isAfter(oldToken.getExpiryDate())){
            throw new InvalidCredential("Refresh token expired");
        }

        User user = oldToken.getUser();

        deleteRefreshToken(oldToken.getToken());
        RefreshToken newToken = jwtUtil.generateRefreshToken(user);

        String newAccessToken = jwtUtil.generateAccessToken(user);

        return new TokenResponse(newAccessToken, newToken.getToken());
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        try {
            refreshTokenRepository.deleteByToken(token);
        } catch (RuntimeException e){
            throw new RuntimeException("Error deleting refresh token " + e);
        }
    }

    @Override
    public void sendResetLink(String email) {
        String token = createResetCode(email);

        //mot doi thanh link cua fe
        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;

        emailService.sendResetLink(email, resetLink);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String key = "reset_code:" + token;
        String email = redisService.get(key);

        if (email == null) {
            throw new RuntimeException("Invalid or expired reset code");
        }

        userRepository.findByEmail(email).ifPresent(user -> {
            user.setHashPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        });
        redisService.delete(key);
    }

    private String createResetCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email not found");
        }
        String token = generateSecureToken();
        String key = "reset_code:" + token;
        redisService.save(
                key,
                email,
                900);
        return token;
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(9000) + 1000);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
