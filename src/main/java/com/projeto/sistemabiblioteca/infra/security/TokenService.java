package com.projeto.sistemabiblioteca.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projeto.sistemabiblioteca.entities.Pessoa;

@Service
public class TokenService {
    private  String secret = "secret"; // o correto é isso ser um valor na variavel de ambiente
    Algorithm algorithm = Algorithm.HMAC256(secret);
    public String generateToken(Pessoa pessoa) {
        try {

            String token = JWT.create().withIssuer("auth-api")
                    .withSubject(pessoa.getEmail().getEndereco())
                    .withClaim("role", pessoa.getFuncao().name())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("erro gerar token", exception);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(-3));
    }

    public DecodedJWT verifyToken(String token) {
        try{
        return  JWT.require(algorithm).withIssuer("auth-api").build().verify(token);
        } catch (TokenExpiredException expired) {
            throw new RuntimeException("Token expirado", expired);
        } catch (JWTVerificationException invalid) {
            throw new RuntimeException("Token inválido", invalid);
        }
    }
}
