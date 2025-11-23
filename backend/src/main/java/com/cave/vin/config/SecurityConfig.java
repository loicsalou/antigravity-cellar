package com.cave.vin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable()) // Disable CSRF for development/API usage
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/index.html", "/assets/**", "/*.js", "/*.css",
                                                                "/*.ico")
                                                .permitAll()
                                                .requestMatchers("/api/user").permitAll() // Allow checking user status
                                                .requestMatchers("/api/**").authenticated() // Protect API
                                                .anyRequest().permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .defaultSuccessUrl("http://localhost:4200/dashboard", true))
                                .logout(logout -> logout
                                                .logoutUrl("/api/logout")
                                                .logoutSuccessHandler((request, response, authentication) -> {
                                                        response.setStatus(200);
                                                })
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"));
                return http.build();
        }
}
