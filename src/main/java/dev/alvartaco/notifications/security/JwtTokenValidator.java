package dev.alvartaco.notifications.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenValidator.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        log.info("JWT Token in JwtTokenValidator: {}", jwt);

        // Check if it's a public endpoint
        if (!isPublicEndpoint(request)) {
            if (jwt != null && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);

                log.info("JWT Token in JwtTokenValidator: {}", jwt);
                try {
                    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                    @SuppressWarnings("deprecation")
                    Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                    log.info(String.valueOf(claims));

                    String email = String.valueOf(claims.get("email"));
                    log.info(email);
                    String authorities = String.valueOf(claims.get("authorities"));
                    List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (ExpiredJwtException e) {
                    throw new BadCredentialsException("Token Expired", e);
                } catch (JwtException e) {
                    throw new BadCredentialsException("Invalid Token", e);
                }
            }
        } else {
            //set to null if it is a public url
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/") || path.startsWith("/error/");
    }
}
