package com.unicam.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;
import java.util.List;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenProvider jwtUtils;
    @Autowired
    private  JwtUserDetailsService userDetailsService;

    public JwtRequestFilter(JwtTokenProvider jwtUtils, JwtUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        String jwtToken = JwtTokenProvider.resolveToken(request);

        if (jwtToken != null) {
            try {
                // Estrai i claims dal token
                Claims claims = jwtUtils.extractAllClaims(jwtToken);
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);
                String municipality = claims.get("municipality", String.class);
                String visitedMunicipality = claims.get("visitedMunicipality", String.class);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtils.validateToken(jwtToken)) {
                        UserCustomDetails userCustomDetails = new UserCustomDetails(username,claims.get("id").toString(), municipality, role, visitedMunicipality);

                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(role));

                        setAuthentication(userCustomDetails, authorities, request);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("Token JWT scaduto", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT scaduto");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                logger.error("Errore nel token JWT", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT non valido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private void setAuthentication(UserCustomDetails userCustomDetails, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userCustomDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
