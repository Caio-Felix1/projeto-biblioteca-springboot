package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimAutor;

public interface DimAutorRepository extends JpaRepository<DimAutor, Long> {
	
	Optional<DimAutor> findByIdNatural(Long id);
}
