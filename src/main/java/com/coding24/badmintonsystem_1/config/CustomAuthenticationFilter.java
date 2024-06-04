package com.coding24.badmintonsystem_1.config;

import com.coding24.badmintonsystem_1.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Map<String, String> requestBody = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String username = requestBody.get("username");
            String password = requestBody.get("password");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", authResult.getName());
        String token = JwtUtil.genToken(claims);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 0);
        responseBody.put("message", "登录成功");
        responseBody.put("token", token);
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 1);
        responseBody.put("message", "用户名或密码错误");
        new ObjectMapper().writeValue(response.getWriter(), responseBody);
    }
}
