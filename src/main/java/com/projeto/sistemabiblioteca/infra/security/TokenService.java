package com.projeto.sistemabiblioteca.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private  String secret = "secret"; // o correto é isso ser um valor na variavel de ambiente
    public String generateToken(Pessoa pessoa) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create().withIssuer("auth-api").withSubject(pessoa.getEmail()).withExpiresAt(genExpirationDate()).sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("erro gerar token",exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(-3));
    }

    public String verifyToken(String token) {
        try{
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return  JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
        }catch (JWTVerificationException exception){
            return "";
        }
    }
}
