package com.projeto.sistemabiblioteca.dashboard.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistemabiblioteca.dashboard.dimensions.DimCategoria;

public interface DimCategoriaRepository extends JpaRepository<DimCategoria, Long> {

	Optional<DimCategoria> findByIdNatural(Long id);
}
