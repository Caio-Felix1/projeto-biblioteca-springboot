package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimIdioma;

public interface DimIdiomaRepository extends JpaRepository<DimIdioma, Long> {
	
	Optional<DimIdioma> findByIdNatural(Long id);
}
