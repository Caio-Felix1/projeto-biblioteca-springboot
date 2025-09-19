package com.projeto.sistemabiblioteca.services;

import com.projeto.sistemabiblioteca.repositories.PessoaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutorizationService implements UserDetailsService {

    @Autowired
    PessoaRepositorio pessoaRepositorio;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return pessoaRepositorio.findByEmail(username);
    }
}
