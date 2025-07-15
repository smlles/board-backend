package com.example.jh.project.first_board.config;

import com.example.jh.project.first_board.service.CustomUserDetailsService;
import com.example.jh.project.first_board.util.JwtAuthenticationFilter;
import com.example.jh.project.first_board.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	  System.out.println("filterChain 메서드 호출됨");
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
        System.out.println("JwtAuthenticationFilter 진입");

        http
        	.cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/", "/api/**", "/swagger-ui/**", "/v3/api-docs/**",
                    "/api/users/register",
                    "/api/users/login",
                    "/api/users",
                    "/api/users/me",
                    "/api/board",
                    "/api/board/**",
                    "/board",
                    "/board/*",
                    "/board/**",
                    "/error",
                    "/favicon.ico"
                    
                ).permitAll()
                .anyRequest().authenticated()
            )
            // 필터 등록은 http 객체에 직접 호출
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
