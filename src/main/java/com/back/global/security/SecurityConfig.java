package com.back.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/favicon.ico").permitAll()
                                .requestMatchers("/resource/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                .headers(
                        headers -> headers
                                .frameOptions(
                                        frameOptions -> frameOptions.sameOrigin()
                                )
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**"));
        return http.build();
    }
}
