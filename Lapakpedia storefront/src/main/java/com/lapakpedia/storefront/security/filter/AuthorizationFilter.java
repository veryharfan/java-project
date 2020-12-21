package com.lapakpedia.storefront.security.filter;

import com.lapakpedia.storefront.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");

        if (token != null
                && !jwtService.isTokenExpired(token)){
            String username = jwtService.getUserNameFromToken(token);
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken usernameAndPasswordToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(usernameAndPasswordToken);
            }
        }
        chain.doFilter(request, response);
    }
}
