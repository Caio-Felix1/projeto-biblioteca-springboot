package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.exceptions.EmailInvalidoException;
import com.projeto.sistemabiblioteca.exceptions.EmailJaCadastradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailInvalido(EmailInvalidoException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(400, ex.getMessage()));
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailJaCadastrado(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(409, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(402, ex.getMessage()));
    }
}
