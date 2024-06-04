package com.coding24.badmintonsystem_1.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.coding24.badmintonsystem_1.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            if (claims != null) {
                String username = (String) claims.get("username");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // 打印用户角色和权限
                System.out.println("User: " + username + ", Authorities: " + userDetails.getAuthorities());
            }
        } catch (JWTVerificationException e) {
            // Token verification failed
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token verification failed");
            return;
        }

        chain.doFilter(request, response);
    }
}
