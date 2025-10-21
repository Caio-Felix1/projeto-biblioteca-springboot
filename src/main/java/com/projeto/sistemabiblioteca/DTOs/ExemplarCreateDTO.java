package com.projeto.sistemabiblioteca.DTOs;
import com.projeto.sistemabiblioteca.entities.enums.EstadoFisico;
import jakarta.validation.constraints.NotNull;

public record ExemplarCreateDTO(@NotNull EstadoFisico estadoFisico,
                                @NotNull Long edicaoId) {
}
