package com.projeto.sistemabiblioteca.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.projeto.sistemabiblioteca.exceptions.EmailInvalidoException;
import com.projeto.sistemabiblioteca.exceptions.EmailJaCadastradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleCamposInvalidos(MethodArgumentNotValidException ex) {
        List<String> mensagens = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage) 
            .sorted()
            .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(mensagens);
    }

	
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
