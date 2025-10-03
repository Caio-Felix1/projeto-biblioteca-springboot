package com.projeto.sistemabiblioteca.infra.security;

import java.io.IOException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
	
    private TokenService tokenSevice;

    private PessoaService  pessoaService;

    public SecurityFilter(TokenService tokenSevice, PessoaService pessoaService) {
        this.tokenSevice = tokenSevice;
        this.pessoaService = pessoaService;
    }

    @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.recoverToken(request);
        if (token != null) {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC256("secret"))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);

            System.out.println("Token inteiro: " + decoded.getToken());
            System.out.println("Header: " + decoded.getHeader());
            System.out.println("Payload: " + decoded.getPayload());
            System.out.println("Assinatura: " + decoded.getSignature());

            System.out.println("Subject: " + decoded.getSubject());
            System.out.println("Role: " + decoded.getClaim("role").asString());

            // pega info direto do token
            String email = decoded.getSubject();


            String role = decoded.getClaim("role").asString().toUpperCase();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);



            UserDetails user = User.builder()
                    .username(email)
                    .password("") // tem que ter senha pra esse objeto funcionar
                    .authorities(authority)
                    .build();

            // cria o objeto de autenticação
            UsernamePasswordAuthenticationToken authAutentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // coloca no contexto do Spring Security e continua o handler
            SecurityContextHolder.getContext().setAuthentication(authAutentication);
            System.out.println("cheguei ao fim do sessao ");
        }

        filterChain.doFilter(request, response);
    }




    private String recoverToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }
        return header.replace("Bearer ", "");
    }
}
