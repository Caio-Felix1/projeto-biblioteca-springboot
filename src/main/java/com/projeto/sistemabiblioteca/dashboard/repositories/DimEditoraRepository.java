package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimEditora;

public interface DimEditoraRepository extends JpaRepository<DimEditora, Long> {
	
	Optional<DimEditora> findByIdNatural(Long id);
}
