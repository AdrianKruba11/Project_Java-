package com.example.biblioteka.config;

import com.example.biblioteka.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;


    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/books",
                                "/register",
                                "/login",
                                "/css/**",
                                "/index",
                                "/index.html",
                                "/adminpanel",
                                "/adminpanel.html",
                                "/addbooks",
                                "/userpanel",
                                "/userpanel.html",
                                "/static/**",
                                "/LogoPans.png",
                                "/Ksiazka1.png",
                                "/Ksiazka2.png",
                                "/Ksiazka3.png",
                                "/Wallpaper.jpg",
                                "/Scripts.js",
                                "/styles.css"
                                        )
                        .permitAll()
                        .anyRequest()
                        .permitAll()) // All other endpoints require authentication
                .formLogin(form -> form
                        .loginPage("/login") // Path to the login page
                        .loginProcessingUrl("/perform_login") // URL for processing login
                        .defaultSuccessUrl("/", true) // Page after successful login
                        .failureUrl("/login?error=true") // Page in case of login failure
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login") // Redirect after logout
                        .permitAll());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}