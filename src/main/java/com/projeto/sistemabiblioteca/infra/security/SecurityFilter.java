package com.projeto.sistemabiblioteca.infra.security;

import com.projeto.sistemabiblioteca.repositories.PessoaRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
	
    @Autowired
    TokenService tokenSevice;

    @Autowired
    PessoaRepositorio  pessoaRepositorio;

    public SecurityFilter(TokenService tokenSevice) {
        this.tokenSevice = tokenSevice;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var email = tokenSevice.verifyToken(token);
            UserDetails pessoa = pessoaRepositorio.findByEmail(email);

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
