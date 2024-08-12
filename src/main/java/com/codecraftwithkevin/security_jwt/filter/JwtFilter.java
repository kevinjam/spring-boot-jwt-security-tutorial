package com.codecraftwithkevin.security_jwt.filter;

import com.codecraftwithkevin.security_jwt.entity.User;
import com.codecraftwithkevin.security_jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull  HttpServletResponse response,
            @NonNull  FilterChain filterChain) throws ServletException, IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        String username = null;
        String jwtTokenHeader =null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            jwtTokenHeader = authorizationHeader.substring(7);
            username = jwtService.extractSubject(jwtTokenHeader);
        }

        if (jwtTokenHeader == null || context.getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwtTokenHeader)){
                var authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
