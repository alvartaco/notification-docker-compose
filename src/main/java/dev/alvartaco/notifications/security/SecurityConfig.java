package dev.alvartaco.notifications.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(this::customizeAuthorization) // use method to configure
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                ;
        return http.build();
    }

    private void customizeAuthorization(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        authorize
                .requestMatchers("/api/**").authenticated() // Restrict api
                .requestMatchers("/auth/**").permitAll() // Allow auth
                .requestMatchers("/web/**").authenticated(); // restricted web
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration ccfg = new CorsConfiguration();
            ccfg.setAllowedOrigins(List.of("http://localhost:3000"));
            ccfg.setAllowedMethods(Collections.singletonList("*"));
            ccfg.setAllowCredentials(true);
            ccfg.setAllowedHeaders(Collections.singletonList("*"));
            ccfg.setExposedHeaders(List.of("Authorization"));
            ccfg.setMaxAge(3600L);
            return ccfg;

        };

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
