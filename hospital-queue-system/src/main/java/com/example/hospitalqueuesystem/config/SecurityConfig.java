package com.example.hospitalqueuesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disable CSRF (important for API)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // allow login/register
                        .requestMatchers("/doctors/**").permitAll() // optional
                        .requestMatchers("/appointments/**").permitAll() // optional
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable()) // disable default login page
                .httpBasic(basic -> basic.disable()); // disable basic auth popup

        return http.build();
    }
}