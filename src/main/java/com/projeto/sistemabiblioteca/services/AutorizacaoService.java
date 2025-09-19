package com.projeto.sistemabiblioteca.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.sistemabiblioteca.entities.Pessoa;

@Service
public class AutorizacaoService implements UserDetailsService {

    private PessoaService pessoaService;
    
    public AutorizacaoService(PessoaService pessoaService) {
    	this.pessoaService = pessoaService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Pessoa p = pessoaService.findByEmail(username);
        
        if (p == null) {
        	throw new UsernameNotFoundException("Erro: usuário não foi encontrado.");
        }
        return p;
    }
}
