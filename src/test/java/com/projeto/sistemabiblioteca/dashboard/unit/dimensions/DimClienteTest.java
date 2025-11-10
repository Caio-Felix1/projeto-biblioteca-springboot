package com.projeto.sistemabiblioteca.dashboard.unit.dimensions;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;
import com.projeto.sistemabiblioteca.entities.Endereco;
import com.projeto.sistemabiblioteca.entities.Estado;
import com.projeto.sistemabiblioteca.entities.Pais;
import com.projeto.sistemabiblioteca.entities.Pessoa;
import com.projeto.sistemabiblioteca.entities.enums.FuncaoUsuario;
import com.projeto.sistemabiblioteca.entities.enums.Sexo;
import com.projeto.sistemabiblioteca.entities.enums.StatusConta;
import com.projeto.sistemabiblioteca.validation.Cpf;
import com.projeto.sistemabiblioteca.validation.Email;
import com.projeto.sistemabiblioteca.validation.Telefone;

public class DimClienteTest {
	
	@Test
	void deveConverterPessoaParaDimClienteCorretamente() {
		Pais pais = new Pais("Brasil");
		
		Estado estado = new Estado("São Paulo", pais);
		
		Endereco endereco = new Endereco(
				"rua teste", 
				"1234", 
				"complemento teste", 
				"bairro teste",
				"00000000",
				"cidade teste",
				estado);
		
		Pessoa pessoa = new Pessoa(
				"Maria Joana", 
				new Cpf("11111111111"), 
				Sexo.FEMININO, 
				FuncaoUsuario.CLIENTE,
				LocalDate.of(1990, 10, 10), 
				LocalDate.of(2025, 10, 10),
				new Telefone("1234567891"), 
				new Email("maria@gmail.com"), 
				"senhaHash",
				StatusConta.ATIVA,
				endereco);
		
		DimCliente dimCliente = new DimCliente(pessoa);
		
		Assertions.assertEquals(Sexo.FEMININO, dimCliente.getSexo());
		Assertions.assertEquals(LocalDate.of(1990, 10, 10), dimCliente.getDtNascimento());
		Assertions.assertEquals(StatusConta.ATIVA, dimCliente.getStatusConta());
		Assertions.assertEquals("cidade teste", dimCliente.getCidade());
		Assertions.assertEquals("São Paulo", dimCliente.getEstado());
		Assertions.assertEquals("Brasil", dimCliente.getPais());
	}
}
