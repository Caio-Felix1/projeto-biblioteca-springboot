package com.projeto.sistemabiblioteca.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projeto.sistemabiblioteca.services.PessoaService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
    @Autowired
    private TokenService tokenSevice;

    @Autowired
    private PessoaService  pessoaService;

    public SecurityFilter(TokenService tokenSevice) {
        this.tokenSevice = tokenSevice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var email = tokenSevice.verifyToken(token);
            UserDetails pessoa = pessoaService.buscarPorEmail(email);

            var authAutentication = new UsernamePasswordAuthenticationToken(pessoa, null, pessoa.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authAutentication);
        }

        filterChain.doFilter(request, response);
    }
    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null ) {
            return null;
        }
        return header.replace("Bearer ", "");
    }
}
