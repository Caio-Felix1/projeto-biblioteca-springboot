package com.projeto.sistemabiblioteca.DTOs;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ExemplarCreateDTO(
		@NotNull(message = "Estado físico é obrigatório")
		EstadoFisico estadoFisico,
		
		@NotNull(message = "Quantidade do estoque é obrigatória")
		@Min(value = 1, message = "O valor mínimo da quantidade de estoque é 1")
		@Max(value = 100, message = "O valor máximo da quantidade de estoque é 100")
		Integer qtdEstoque,
                                
		@NotNull(message = "Edição é obrigatória") 
		Long edicaoId) {
}
