package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCliente;

public interface DimClienteRepository extends JpaRepository<DimCliente, Long> {
	
	Optional<DimCliente> findByIdNatural(Long id);
}
