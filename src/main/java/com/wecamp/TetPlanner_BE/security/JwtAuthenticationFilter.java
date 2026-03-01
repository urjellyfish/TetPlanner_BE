package com.wecamp.TetPlanner_BE.security;

import com.wecamp.TetPlanner_BE.dto.BaseResponse;
import com.wecamp.TetPlanner_BE.entity.enums.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No Authorization header → treat as anonymous (public endpoints)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        // Token present but invalid → 401
        if (token.isEmpty() || !jwtUtil.validateJwtToken(token)) {
            writeAuthError(response, ErrorCode.ACCESS_TOKEN_INVALID);
            return;
        }

        String userId = jwtUtil.getUserFromToken(token);


        UUID principalId;
        try {
            principalId = UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            writeAuthError(response, ErrorCode.USER_ID_INVALID);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                principalId,
                null,
                null
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        return uri.startsWith("/api/auth/")
                || uri.contains("/v3/api-docs")
                || uri.contains("/swagger-ui")
                || uri.contains("/swagger-resources")
                || uri.contains("/webjars");
    }

    private void writeAuthError(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {

        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                new BaseResponse<>(false, errorCode.getMessage(), null)
        );
    }
}