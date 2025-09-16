package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.enums.FuncaoPessoa;

public record RegisterDTO(String email, String senha , FuncaoPessoa funcao) {
}
