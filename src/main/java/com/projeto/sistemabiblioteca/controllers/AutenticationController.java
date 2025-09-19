package com.projeto.sistemabiblioteca.controllers;

import com.projeto.sistemabiblioteca.DTOs.AutenticationDTO;
import com.projeto.sistemabiblioteca.DTOs.LoginResponseDTO;
import com.projeto.sistemabiblioteca.DTOs.RegisterDTO;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.repositories.PessoaRepositorio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.projeto.sistemabiblioteca.infra.security.TokenService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AutenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PessoaRepositorio repositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>login(@Valid @RequestBody AutenticationDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.senha()
        );
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Pessoa) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/register")
    public  ResponseEntity<String> register(@Valid @RequestBody RegisterDTO request){
        if (repositorio.findByEmail(request.email()) != null) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        String encryptedPassword = passwordEncoder.encode(request.senha());

        Pessoa pessoa = new Pessoa(request.email(),encryptedPassword,request.funcao());

        this.repositorio.save(pessoa);
        return  ResponseEntity.ok("Registro efetuado com sucesso");
    }
}
