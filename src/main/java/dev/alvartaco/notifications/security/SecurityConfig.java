package dev.alvartaco.notifications.security;

import dev.alvartaco.notifications.service.secure.UserServiceImplementation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserServiceImplementation userServiceImplementation;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(UserServiceImplementation userServiceImplementation,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.userServiceImplementation = userServiceImplementation;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Use cookies for CSRF
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // Restrict api
                        .requestMatchers("/web/**").authenticated() // restricted web
                        .requestMatchers("/auth/**").permitAll() // Allow auth
                        .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll() // Allow signup
                        .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()// Allow signin
                        .requestMatchers("/error/**").permitAll() // Allow the error pages!
                        .requestMatchers(HttpMethod.GET, "/auth/csrf").permitAll()// Allow csrf receive
                        .anyRequest().permitAll()) // everything else needs to be public
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                        .logoutSuccessHandler(logoutSuccessHandler()) // Custom logoutSuccessHandler
                        .invalidateHttpSession(true)
                )
                .addFilterBefore(new JwtTokenCookieFilter(userServiceImplementation), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration ccfg = new CorsConfiguration();
            ccfg.setAllowedOrigins(List.of("http://localhost:3000"));
            ccfg.setAllowedMethods(Collections.singletonList("*"));
            ccfg.setAllowCredentials(true);
            ccfg.setAllowedHeaders(Collections.singletonList("*"));
            ccfg.setExposedHeaders(List.of("Authorization","X-CSRF-TOKEN"));//add X-CSRF-TOKEN
            ccfg.setMaxAge(3600L);
            return ccfg;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            // Invalidate the JWT cookie
            Cookie jwtCookie = new Cookie("jwtToken", null);
            jwtCookie.setMaxAge(0);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true); // Important for security
            //jwtCookie.setSecure(true); // If using HTTPS
            response.addCookie(jwtCookie);

            // Redirect to the login page (or wherever you want)
            response.sendRedirect("http://localhost:3000/");
        };
    }
}