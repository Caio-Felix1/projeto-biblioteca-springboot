package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimExemplar;

public interface DimExemplarRepository extends JpaRepository<DimExemplar, Long> {
	
	Optional<DimExemplar> findByIdNatural(Long id);
}
