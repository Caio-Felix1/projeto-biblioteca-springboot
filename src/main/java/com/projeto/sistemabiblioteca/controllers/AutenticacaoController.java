package com.projeto.sistemabiblioteca.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.sistemabiblioteca.DTOs.AutenticacaoDTO;
import com.projeto.sistemabiblioteca.DTOs.LoginResponseDTO;
import com.projeto.sistemabiblioteca.DTOs.RegistroDTO;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.infra.security.TokenService;
import com.projeto.sistemabiblioteca.services.PessoaService;
import com.projeto.sistemabiblioteca.validation.Email;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AutenticacaoController {
	
	private AuthenticationManager authenticationManager;
	
	private PessoaService pessoaService;

    private PasswordEncoder passwordEncoder;

    private TokenService tokenService;
    
    public AutenticacaoController(AuthenticationManager authenticationManager, PessoaService pessoaService,
			PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.authenticationManager = authenticationManager;
		this.pessoaService = pessoaService;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}


	@PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AutenticacaoDTO request) {
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.senha()
        );
		Authentication auth = this.authenticationManager.authenticate(usernamePassword);
		String token = tokenService.generateToken((Pessoa) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@Valid @RequestBody RegistroDTO request) {
    	/*
        pessoaService.verificarEmailDisponivel(request.email());

        Email email = new Email(request.email());

        String encryptedPassword = passwordEncoder.encode(request.senha());

        Pessoa pessoa = new Pessoa(email, encryptedPassword, request.funcao());
		*/
    	
    	pessoaService.cadastrarUsuario(request);
    	
    	return ResponseEntity.ok("Registro efetuado com sucesso.");

    }
}
