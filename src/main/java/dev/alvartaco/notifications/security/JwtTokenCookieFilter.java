package dev.alvartaco.notifications.security;

import dev.alvartaco.notifications.service.secure.UserServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtTokenCookieFilter extends OncePerRequestFilter {

    private final UserServiceImplementation userServiceImplementation;

    public JwtTokenCookieFilter(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null && JwtProvider.validateToken(jwtToken)) {
            String email = JwtProvider.getEmailFromJwtToken(jwtToken);

            UserDetails userDetails = userServiceImplementation.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            extendCookieExpiration(request, response);// Extend the cookie's expiration time
        }
        filterChain.doFilter(request, response);
    }

    private void extendCookieExpiration(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .filter(cookie -> "jwtToken".equals(cookie.getName()))
                    .findFirst()
                    .ifPresent(cookie -> {
                        cookie.setMaxAge(2 * 60); // Extend expiration by 1 hour
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    });
        }
    }
}
