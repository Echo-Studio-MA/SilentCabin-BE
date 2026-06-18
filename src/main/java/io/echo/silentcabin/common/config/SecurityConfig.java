package io.echo.silentcabin.common.config;

import io.echo.silentcabin.common.config.entrypoint.JwtAccessDeniedHandler;
import io.echo.silentcabin.common.config.entrypoint.JwtAuthenticationEntryPoint;
import io.echo.silentcabin.common.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/v1/users/register", "/api/v1/users/login").anonymous()
                                .requestMatchers("/api/v1/users/refresh").permitAll()
                                .requestMatchers("/api/v1/users/logout").authenticated()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(exp ->exp
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //jwt는 세션 사용x
                .build();
    }
}