package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEdicao;

public interface DimEdicaoRepository extends JpaRepository<DimEdicao, Long> {
	
	Optional<DimEdicao> findByIdNatural(Long id);
}
