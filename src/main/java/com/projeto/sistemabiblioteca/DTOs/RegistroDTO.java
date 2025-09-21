package com.projeto.sistemabiblioteca.DTOs;

import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;

public record RegistroDTO(String email, String senha , FuncaoUsuario funcao) {
}
