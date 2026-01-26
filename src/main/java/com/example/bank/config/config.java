package com.example.bank.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.net.http.HttpRequest;


@Configuration
@EnableWebSecurity
 class ConfigConfiguration {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable());
        return http.build();


    }
    
}

