//package com.nwg.ezpay.security;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////
/////**
//// * Provides BCryptPasswordEncoder bean for secure hashing of PINs.
//// * Author: Aziz Mehevi
//// */
////@Configuration
////public class PasswordConfig {
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////}
//
////package com.nwg.ezpay.security;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.web.SecurityFilterChain;
////
////@Configuration
////public class SecurityConfig {
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////            .csrf().disable()
////            .authorizeHttpRequests()
////            .anyRequest().permitAll();
////        return http.build();
////    }
////}
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.http.HttpMethod;
// 
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                // -----------------------------
//                // Public endpoints
//                // -----------------------------
//                .requestMatchers(HttpMethod.POST, "/api/account-holders").permitAll() // signup
//                .requestMatchers("/").permitAll() // root welcome page
//
//                // -----------------------------
//                // Admin-only endpoints
//                // -----------------------------
//                .requestMatchers(HttpMethod.GET, "/api/account-holders/all/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/api/account-holders/{id}").hasRole("ADMIN")
//                .requestMatchers("/api/beneficiaries/**").hasRole("ADMIN")
//                .requestMatchers("/api/payment-instructions/all/**").hasRole("ADMIN")
//                .requestMatchers(HttpMethod.GET, "/api/payment-instructions/status/**").hasRole("ADMIN")
//
//                // -----------------------------
//                // User-only endpoints
//                // -----------------------------
//                .requestMatchers("/api/account-holders/**").hasRole("USER")
//                .requestMatchers("/api/payment-instructions/**").hasRole("USER")
//
//                // -----------------------------
//                // Everything else requires authentication
//                // -----------------------------
//                .anyRequest().authenticated()
//            )
//            .httpBasic(); // HTTP Basic will prompt login
//
//        return http.build();
//    }
//}
