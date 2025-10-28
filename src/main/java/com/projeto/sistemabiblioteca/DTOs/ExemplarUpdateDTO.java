package com.projeto.sistemabiblioteca.DTOs;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;

import jakarta.validation.constraints.NotNull;

public record ExemplarUpdateDTO(
		@NotNull(message = "Estado físico é obrigatório")
		EstadoFisico estadoFisico,
                                
		@NotNull(message = "Edição é obrigatória") 
		Long edicaoId) {
}
