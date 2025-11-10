package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimTitulo;

public interface DimTituloRepository extends JpaRepository<DimTitulo, Long> {
	
	Optional<DimTitulo> findByIdNatural(Long id);
}
