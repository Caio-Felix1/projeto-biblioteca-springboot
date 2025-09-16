package com.projeto.sistemabiblioteca.enums;

public enum FuncaoPessoa
{
    CLIENTE("cliente"),
    BIBLIOTECARIO("bibliotecario"),
    ADMINISTRADOR("administrador"),;

    private String funcao;

    FuncaoPessoa(String funcao){
        this.funcao = funcao;
    }

    public String getFuncao() {
        return funcao;
    }
}
