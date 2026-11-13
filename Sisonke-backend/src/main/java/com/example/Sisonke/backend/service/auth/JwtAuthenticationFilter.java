package com.example.Sisonke.backend.service.auth;

import com.example.Sisonke.backend.Repository.UserRepository;
import com.example.Sisonke.backend.models.user.entity.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader =
            request.getHeader("Authorization");

        if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(7);

        try {
            String email = jwtService.extractUsername(token);

            if (email != null &&
                SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                User user = userRepository.findByEmailIgnoreCase(email)
                    .orElse(null);

                if (user != null) {

                    UserDetails userDetails =
                        org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPasswordHash())
                            .authorities("ROLE_MEMBER")
                            .build();

                    if (jwtService.isValid(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                userDetails.getAuthorities()
                            );

                        authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                    }
                }
            }

        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}

