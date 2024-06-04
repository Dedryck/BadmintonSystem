package com.coding24.badmintonsystem_1.config;


import com.coding24.badmintonsystem_1.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    private final UserService userService;


    public WebSecurityConfig(@Lazy UserService userService) {
        this.userService = userService;

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/js/**", "/css/**", "/images/**");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5174");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, userService);

        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index", "/toLogin", "/public/**", "/js/**", "/css/**", "/images/**", "/home", "/instructors").permitAll()
                        .requestMatchers("/api/login", "/api/users/register", "/api/users/check-username"
                                ,"/api/court-bookings","/api/court").permitAll()

                        .requestMatchers("/api/equipment-management/**", "/api/court-bookings/edit"
                                , "/api/court-bookings/delete/**", "/api/feedback-management/**"
                                , "/api/instructor-management/**", "/api/staff-management/**","/api/court-management/**").hasRole("ADMIN")

                        .requestMatchers("/api/feedback/**", "/api/equipment-rentals/**"
                                , "/api/court-bookings/add", "/api/court-bookings"
                                , "/api/instructors","/api/home", "/api/court-bookings/**","/api/user-info").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.accessDeniedPage("/noPermit"))
                .rememberMe(rememberMe -> rememberMe.userDetailsService(userService))
                .userDetailsService(userService);

        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
