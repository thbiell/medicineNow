package com.global.MedicineNow.security;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.global.MedicineNow.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationUserFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	var token = getToken(request);

    	if (token != null) {
    	    var medico = tokenService.validateAndGetMedicoBy(token);
    	    var usuario = tokenService.validateAndGetUserBy(token);

    	    if (medico != null) {
    	        // É um médico
    	        Authentication authM = new UsernamePasswordAuthenticationToken(medico.getEmail(), null);
    	        SecurityContextHolder.getContext().setAuthentication(authM);
    	    } else if (usuario != null) {
    	        // É um usuário
    	        Authentication auth = new UsernamePasswordAuthenticationToken(usuario.getUsername(), null, usuario.getAuthorities());
    	        SecurityContextHolder.getContext().setAuthentication(auth);
    	    }
    	}
    

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String prefix = "Bearer ";
        var header = request.getHeader("Authorization");

        if (header == null || header.isEmpty() || !header.startsWith(prefix)) {
            return null;
        }

        return header.replace(prefix, "");
    }
}

